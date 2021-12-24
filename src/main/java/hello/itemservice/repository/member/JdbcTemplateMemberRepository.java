package hello.itemservice.repository.member;

import hello.itemservice.domain.member.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


public class JdbcTemplateMemberRepository implements MemberRepository{

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcTemplateMemberRepository(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Member save(Member member) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("member");

        Map<String, Object> parameter = new HashMap<>();
        parameter.put("id",member.getId());
        parameter.put("pwd",member.getPwd());
        parameter.put("name",member.getName());
        jdbcInsert.execute(parameter);
        return member;
    }

    @Override
    public Optional<Member> findById(String id) {
        List<Member> result = jdbcTemplate.query("select * from member where id =?", memberRowMapper(),id);
        return result.stream().findAny();
    }

    @Override
    public Optional<Member> findByName(String name) {
        List<Member> result = jdbcTemplate.query("select * from member where name =?", memberRowMapper(), name);
        return result.stream().findAny();
    }

    @Override
    public List<Member> findAll() {
        return jdbcTemplate.query("select * from member",memberRowMapper());
    }


    public Optional<String> login(Member member){
        List<Member> result = jdbcTemplate.query("select * from MEMBER where ID = ?", memberRowMapper(),member.getId());
        Optional<Member> dbMember = result.stream().findAny();
        Optional<String> loginID = null;
        if (dbMember.isPresent()){
                loginID = Optional.ofNullable(member.getId());
            } else {
                throw new IllegalStateException("비밀번호 오류");
            }
                return loginID;
    }

    private RowMapper<Member> memberRowMapper(){
        return (rs, rowNum) -> {
            Member member = new Member();
            member.setId(rs.getString("id"));
            member.setPwd(rs.getString("pwd"));
            member.setName(rs.getString("name"));
            return member;
        };
    }
}
