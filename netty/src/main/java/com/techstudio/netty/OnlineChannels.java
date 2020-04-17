package com.techstudio.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelId;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GlobalEventExecutor;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class OnlineChannels {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance(OnlineChannels.class);
    private static final String GROUP_SPLIT = ".";

    public static final AttributeKey<Boolean> ATTR_KEY_READY = AttributeKey.newInstance("ready");
    public static final AttributeKey<String> ATTR_KEY_GROUP = AttributeKey.newInstance("group");
    public static final AttributeKey<String> ATTR_KEY_ID = AttributeKey.newInstance("id");
    public static final AttributeKey<String> ATTR_KEY_PLATFORM = AttributeKey.newInstance("platform");
    public static final AttributeKey<String> ATTR_KEY_DEVICE = AttributeKey.newInstance("deviceId");
    public static final AttributeKey<Long> ATTR_KEY_LAST_ACTIVE_TIME = AttributeKey.newInstance("lastActiveTime");
    public static final int INACTIVE_TIME = 6 * 60 * 1000;//6分钟

    // 在线成员
    private static ChannelGroup online = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private static ConcurrentMap<String, HashSet<ChannelId>> idsMapper = PlatformDependent.newConcurrentHashMap();
    private static ConcurrentMap<String, HashSet<ChannelId>> groupsMapper = PlatformDependent.newConcurrentHashMap();

    //重发队列
    private static ConcurrentLinkedQueue<SendFailData> resendQueue = new ConcurrentLinkedQueue<>();

    private AtomicBoolean runResendThread = new AtomicBoolean(true);

    public OnlineChannels() {
        // 消息重发线程
        ResendThread resendThread = new ResendThread();
        resendThread.start();
    }


    /**
     * 清理在线连接
     */
    public static void clear() {
        online.clear();
        idsMapper.clear();
        groupsMapper.clear();
    }

    public static void registerChannel(Channel channel) {
        channel.attr(ATTR_KEY_READY).set(false);
        channel.attr(ATTR_KEY_LAST_ACTIVE_TIME).set(System.currentTimeMillis());
        //注册Channel
        online.add(channel);
    }


    /**
     * 注册连接到在线列表
     *
     * @param channel
     * @param group
     * @param id
     */
    public static void register(Channel channel, String group, String id, String platform, String deviceId) {
        channel.attr(ATTR_KEY_READY).set(true);
        channel.attr(ATTR_KEY_GROUP).set(group);
        channel.attr(ATTR_KEY_ID).set(id);
        channel.attr(ATTR_KEY_PLATFORM).set(platform);
        channel.attr(ATTR_KEY_DEVICE).set(deviceId);
        registerMapper(channel, group, id);
    }

    /**
     * 下线处理
     *
     * @param channel
     * @param group
     * @param id
     */
    public static void unregister(Channel channel, String group, String id) {
        unregisterMapper(channel, group, id);
        online.remove(channel);
    }

    public static void registerMapper(Channel channel, String group, String id) {
        String groupKey = group + GROUP_SPLIT;
        //注册id
        HashSet<ChannelId> channelIds = idsMapper.get(id);
        if (channelIds == null) {
            channelIds = new HashSet<ChannelId>();
            idsMapper.put(id, channelIds);
        } else {
            //todo 暂时改为一id只有一个socket连接
            for (ChannelId oldid : channelIds) {
                Channel oldChannel = online.find(oldid);
                oldChannel.close();
            }
            channelIds.clear();
        }
        channelIds.add(channel.id());

        //注册group
        HashSet<ChannelId> groupChannelIds = groupsMapper.computeIfAbsent(groupKey, k -> new HashSet<>());
        groupChannelIds.add(channel.id());
    }

    /**
     * 清理映射
     *
     * @param channel
     * @param group
     * @param id
     */
    public static void unregisterMapper(Channel channel, String group, String id) {
        if (id != null) {
            HashSet<ChannelId> channelIds = idsMapper.get(id);
            if (channelIds != null) {
                channelIds.remove(channel.id());
                if (channelIds.isEmpty()) {
                    idsMapper.remove(id);
                }
            }
        }
        if (group != null) {
            String groupKey = group + GROUP_SPLIT;
            HashSet<ChannelId> groupChannelIds = groupsMapper.get(groupKey);
            if (groupChannelIds != null) {
                groupChannelIds.remove(channel.id());
                if (groupChannelIds.isEmpty()) {
                    groupsMapper.remove(groupKey);
                }
            }
        }
    }

    /**
     * 获取指定id的连接活动状态
     *
     * @param id
     * @return
     */
    public static boolean isMobileActive(String id) {
        if (id != null) {
            HashSet<ChannelId> channelIds = idsMapper.get(id);
            if (channelIds != null && !channelIds.isEmpty()) {
                for (ChannelId channelId : channelIds) {
                    Channel channel = online.find(channelId);
                    String platform = channel.attr(ATTR_KEY_PLATFORM).get();
                    String deviceId = channel.attr(ATTR_KEY_DEVICE).get();
                    if (deviceId != null || "android".equals(platform) || "ios".equals(platform)) {
                        return channel.isActive();
                    }
                }
            } else {
                return false;
            }
        }
        return false;
    }

    public static Map<String, HashSet<ChannelId>> getIdsMapper() {
        return idsMapper;
    }

    public static Map<String, HashSet<ChannelId>> getGroupsMapper() {
        return groupsMapper;
    }

    /**
     * 按id查找ChannelId
     *
     * @param id
     * @return 返回null表示id离线
     */
    public static Set<ChannelId> findId(String id) {
        return idsMapper.getOrDefault(id, null);
    }

    /**
     * 传入多个id查找ChannelId
     *
     * @param ids
     * @return
     */
    public static Set<ChannelId> findIds(List<String> ids) {
        return findIds(ids, null);
    }

    /**
     * @param ids        要查找的id列表
     * @param offlineIds 离线列表，调用时传入空列表，函数会把离线id往里塞
     * @return
     */
    public static Set<ChannelId> findIds(List<String> ids, List<String> offlineIds) {
        HashSet<ChannelId> channelIds = new HashSet<>();
        for (String id : ids) {
            if (idsMapper.containsKey(id)) {
                HashSet<ChannelId> userChannelIds = idsMapper.get(id);
                if (userChannelIds != null) {
                    channelIds.addAll(userChannelIds);
                }
            } else if (offlineIds != null) {
                offlineIds.add(id);
            }
        }
        return channelIds;
    }

    /**
     * 查找指定group下的ChannelId
     *
     * @param group
     * @return
     */
    public static Set<ChannelId> findGroup(String group) {
        String groupKey = group + GROUP_SPLIT;
        HashSet<ChannelId> groupChannelIds = new HashSet<>();
        Iterator<Map.Entry<String, HashSet<ChannelId>>> iterator = groupsMapper.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, HashSet<ChannelId>> m = iterator.next();
            String groupName = m.getKey();
            if (groupName.indexOf(groupKey) == 0 && m.getValue() != null) {
                groupChannelIds.addAll(m.getValue());
            }
        }
        return groupChannelIds;
    }


    /**
     * 记录Channel最后一次活动时间
     *
     * @param channel
     */
    public static void refreshLastActiveTime(Channel channel) {
        if (channel != null) {
            channel.attr(ATTR_KEY_LAST_ACTIVE_TIME).set(System.currentTimeMillis());
        }
    }

    /**
     * 检测不活动连接
     */
    public static void checkInactiveChannel() {
        for (Channel channel : online) {
            Long lastActiveTime = channel.attr(ATTR_KEY_LAST_ACTIVE_TIME).get();
            if (System.currentTimeMillis() - lastActiveTime > INACTIVE_TIME) {
                String group = channel.attr(ATTR_KEY_GROUP).get();
                String id = channel.attr(ATTR_KEY_ID).get();
                unregisterMapper(channel, group, id);
                channel.close();
            }
        }
    }

    /**
     * 限制一个设备一条连接
     *
     * @param deviceId
     */
    public static void limitDeviceConnection(String deviceId) {
        if (deviceId != null) {
            for (Channel channel : online) {
                String channelDeviceId = channel.attr(ATTR_KEY_DEVICE).get();
                if (deviceId.equals(channelDeviceId)) {
                    channel.close();
                }
            }
        }
    }


    /**
     * 发送失败后的回调函数
     */
    class SendFailCallBack implements ChannelFutureListener {

        @Override
        public void operationComplete(ChannelFuture future) throws Exception {

        }
    }

    private class SendFailData {

    }

    class ResendThread extends Thread {

        public ResendThread() {
            super("resend-thread");
            super.setDaemon(true);
        }

        @Override
        public void run() {

        }
    }


}



