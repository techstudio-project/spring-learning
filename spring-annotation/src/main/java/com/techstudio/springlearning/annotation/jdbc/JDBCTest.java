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
        try (Connection conn = getConnection()) {
            transactionTest(conn);
        }
    }

    private static void transactionTest(Connection conn) throws SQLException {
        // 手动开启事务
        conn.setAutoCommit(false);
        PreparedStatement ps = null;
        try {

            String sql = "insert into ses_assessment_stdhour values(?,?,?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1, "qwer");
            ps.setInt(2, 500600);
            ps.setDouble(3, 1.2);
            int rows = ps.executeUpdate();
            conn.commit();
            System.out.println("effect rows :" + rows);
        }
        catch (Exception e) {
            conn.rollback();
        }
        finally {
            if (ps != null) {
                ps.close();
            }
        }
    }

    private static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://10.1.248.147:3306/am_general_qhln?useUnicode=true&characterEncoding=utf8" +
                "&useSSL=false&serverTimezone=GMT%2B8";
        String username = "am";
        String password = "amdbadmin,";
        return DriverManager.getConnection(url, username, password);
    }

    private static void executeQueryTest(Connection conn) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "select * from ses_assessment_stat where `month`= ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, "2020-01");
            rs = ps.executeQuery();
            List<Map<String, Object>> list = new ArrayList<>();
            ps.getResultSet();
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
                try {
                    rs.close();
                }
                catch (Exception ignore) {
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                }
                catch (Exception ignore) {
                }
            }
        }
    }

}
