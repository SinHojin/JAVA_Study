package daelim.spring_ch03;

import java.time.LocalDateTime;
import java.util.Collection;

public class MemberRegisterService {

    private MemberDao memberDao;

    // DI는 의존하는 객체를 직접 생성하는 대신 의존 객체를 전달받는 방식 사용
    // 즉, 생성자를 통해 의존(Dependency)하고 있는 MemberDao객체를 주입(Injection)받은 것.
    public MemberRegisterService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public void regist(RegisterRequest req) {
        // 이메일로 회원 데이터 조회
        Member member = memberDao.selectByEmail(req.getEmail());
        if(member != null) {
            // 이미 같은 이메일 가진 회원이 존재
            throw new DuplicationMemberException("Duplication Email : "+ req.getEmail());
        }

        Member newMem = new Member(req.getEmail(), req.getPassword(), req.getName(), LocalDateTime.now());
        memberDao.insert(newMem);
    }

}