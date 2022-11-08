package gukjin.servlet.basic.domain.member;

import gukjin.servlet.domain.member.Member;
import gukjin.servlet.domain.member.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

// JUNIT 5부터는 테스트 클래스에 public 없어도 됨
class MemberRepositoryTest {

    MemberRepository memberRepository = MemberRepository.getInstance();

    @AfterEach
    void afterEach(){
        memberRepository.clearStore();
    }

    @Test
    public void save() throws Exception {
        Member member = new Member("hello", 20);
        memberRepository.save(member);

        memberRepository.findById(member.getId());

        assertThat(member).isEqualTo(member);
    }

    @Test
    void findAll(){
        Member member = new Member("member1", 20);
        Member member2 = new Member("member2", 30);
        memberRepository.save(member);
        memberRepository.save(member2);

        List<Member> result = memberRepository.findAll();

        assertThat(result.size()).isEqualTo(2);
        assertThat(result).extracting("username").containsExactly("member1", "member2");
    }

}