package com.techstudio.springlearning.annotation.jdbc;

import com.alibaba.fastjson.JSON;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lj
 * @date 2020/2/19
 */
public class JDBCTest {

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = "jdbc:mysql://10.1.248.147:3306/am_general_qhln?useUnicode=true&characterEncoding=utf8" +
                "&useSSL=false&serverTimezone=GMT%2B8";
        String username = "am";
        String password = "amdbadmin,";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {

            conn = DriverManager.getConnection(url, username, password);
            String sql = "select * from ses_assessment_stat where `month`= ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, "2020-01");

            rs = ps.executeQuery();

            List<Map<String, Object>> list = new ArrayList<>();
            while (rs.next()) {
                String id = rs.getString("id");
                String totalScore = rs.getString("total_score");
                Map<String, Object> map = new HashMap<>();
                map.put("id", id);
                map.put("totalScore", totalScore);
                list.add(map);
            }

            System.out.println(JSON.toJSONString(list));

        }
        finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
        }

    }

}
