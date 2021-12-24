package hello.itemservice.repository.member;

import hello.itemservice.domain.member.Member;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcMemberRepository implements MemberRepository {
    // 가장 기본적인 형태의 jdbc
    private final DataSource dataSource;

    public JdbcMemberRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private Connection getConnection(){
        // 데이터베이스 커넥션을 여러개로 받지 않고 하나의 객체로
        // 다 같이 공유하는 방법
        return DataSourceUtils.getConnection(dataSource);
    }
    private void close(Connection conn) throws SQLException {
        DataSourceUtils.releaseConnection(conn, dataSource);
    }
    private void close(Connection conn, PreparedStatement pstmt, ResultSet rs)
    {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (pstmt != null) {
                pstmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (conn != null) {
                close(conn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Member save(Member member) {
        String sql = "insert into member values(?,?,?)";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {

            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, member.getId());
            pstmt.setString(2, member.getPwd());
            pstmt.setString(3, member.getName());
            int result = pstmt.executeUpdate();
            if (result < 1) {
                throw new SQLException("id 조회 실패");
            }
            return member;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn,pstmt,rs);
        }
    }

    @Override
    public Optional<Member> findById(String id) {
        String sql = " select * from member where ID=?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                Member member = new Member();

                member.setId(rs.getString("id"));
                member.setPwd(rs.getString("pwd"));
                member.setName(rs.getString("name"));
                return Optional.of(member);
            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn,pstmt,rs);
        }
    }

    @Override
    public Optional<Member> findByName(String name) {
        return Optional.empty();
    }

    @Override
    public List<Member> findAll() {
        String sql = " select * from member";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Member> members = new ArrayList<>();
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Member member = new Member();
                member.setId(rs.getString("id"));
                member.setPwd(rs.getString("pwd"));
                member.setName(rs.getString("name"));
                members.add(member);
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn,pstmt,rs);
        }
            return members;
    }

    @Override
    public Optional<String> login(Member member) {
        return Optional.empty();
    }


}
