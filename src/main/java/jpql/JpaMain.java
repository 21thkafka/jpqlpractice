package jpql;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class JpaMain {

    public static void main(String[] args){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            //join 확인을 위해 team과 연관
            Team teamA = new Team();
            teamA.setName("팀A");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("팀B");
            em.persist(teamB);

            Member member = new Member();
            member.setUsername("회원 1");
            member.setAge(100);
            member. setTeam(teamA);
            member.setType(MemberType.ADMIN);

            Member member2 = new Member();
            member2.setUsername("회원 2");
            member2.setTeam(teamA);

            em.persist(member);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("회원 3");
            member3.setTeam(teamB);
            em.persist(member3);

            //TypeQuery - 제네릭
        //    TypedQuery<Member> query = em.createQuery("select m from Member m", Member.class);
       //     TypedQuery<String> query2 = em.createQuery("select m.name from Member m", String.class);
            //name은 string, age는 int으로 자료형이 달라 TypedQuery 사용할 수 없음. 이렇게 반환타입이 명확하지 않은 경우 Query 사용해야함.
        //    Query query3 = em.createQuery("select m.name, m.age from Member m");

            //값이 여러개일 경우
       /*     List<Member> resultList = query.getResultList();
            for(Member member1 : resultList){
                System.out.println("member : " + member1);
            }

            //값이 하나일 경우, 값이 없어도 하나 이상이어도 Exception 발생, 꼭 사용하면 try 처리해야함
            try{
                Member singleResult = query.getSingleResult();
            } catch (NoResultException e){
                //return null;

            }*/

    /*        Member singleResult = em.createQuery("select m from Member m where m.username = :username", Member.class)
                    .setParameter("username", "member1")
                    .getSingleResult();
            System.out.println("singleResult : " + singleResult);*/

            em.flush();
            em.clear();

            //프로젝션
       /*     List<Member> result = em.createQuery("select m from Member m", Member.class)
                    .getResultList();

            Member findMember = result.get(0);
            findMember.setAge(20);
            //result 결과값 모두 영속성에서 관리됨.
            System.out.println("findMember age : " + findMember.getAge());*/

            // 둘다 같은 조인문이 나가지만 첫번째(묵시적 조인)는 예측하기 힘들기 떄문에 후자 방식으로 사용 권장
            //List<Team> team = em.createQuery("select m.team from Member m", Team.class)
        //    List<Team> team = em.createQuery("select t from Member m join m.team t", Team.class)
        //            .getResultList();

            //임베디드 타입 프로젝션 - 소속되어 있는 엔티티에서 꺼내와야함 from Order는 가능 / from Address는 불가능
        //    em.createQuery("select o.address from Order o", Address.class)
        //            .getResultList();

            //스칼라 타입 프로젝션
            //객체 값을 받아야하는 경우 Object[]로 받는 방법 - 비추
/*            List<Object[]> resultList = em.createQuery("select distinct m.username, m.age from Member m")
                    .getResultList();

            Object[] results = resultList.get(0);
            System.out.println("username = " + results[0]);
            System.out.println("age = " + results[1]);
*/
            // new 명령어로 조회 - 많이 사용
  /*          List<MemberDTO> resultList = em.createQuery("select new jpql.MemberDTO(m.username, m.age) from Member m", MemberDTO.class)
                    .getResultList();
            MemberDTO memberDTO = resultList.get(0);
            System.out.println("memberDTO username = " + memberDTO.getUsername());
            System.out.println("memberDTO age = " + memberDTO.getAge());

   */
    /*        // 페이징
            for(int i = 1; i < 100; i++){
                Member user = new Member();
                user.setUsername("member" + i);
                user.setAge(i);
                em.persist(user);
            }

            List<Member> result = em.createQuery("select m from Member m order by m.age desc")
                    .setFirstResult(1)
                    .setMaxResults(10)
                    .getResultList();

            System.out.println("result.size = " + result.size());;
            for (Member member1 : result){
                System.out.println("member1 = " + member1.getUsername());
            }
     */
            //조인
            //String query = "select m from Member m left outer join m.team t";
            //String query = "select m from Member m, Team t where m.username = t.name"; // 세타 조인
            //String query = "select m from Member m left join m.team t on t.name = 'teamA'"; //외부 조인 on 절 지원
     /*       String query = "select m from Member m left join Team t on m.username = t.name"; //외부 조인의 on 절 다른표현
            List<Member> result = em.createQuery(query, Member.class)
                    .getResultList();

            System.out.println("result : " + result.size());
      */
       //jpql 타입 표현과 기타식
       //     String query = "select m.username, 'HELLO', true from Member m" +
       //             " where m.type = jpql.MemberType.USER";   // 쿼리 안에 직접 명시할경우 패키지명까지 넣어야함
       //     String query = "select m.username, 'HELLO', true from Member m" +
            //            " where m.type = :memberType";
       /*     String query = "select m.username, 'HELLO', true from Member m" +
                    " where m.username is not null";    //is not null, between 가능

            List<Object[]> result = em.createQuery(query)
                    .setParameter("memberType", MemberType.USER)    //enum 파라메터로 넣을 수 있음
                            .getResultList();

            for(Object[] objects : result){
                System.out.println("objects = " + objects[0]);
                System.out.println("objects = " + objects[1]);
                System.out.println("objects = " + objects[2]);
            }
        */
            //조건식
            //case when 절
//            String query =
//                    "select " +
//                            "case when m.age <= 10 then '학생요금' "+
//                            "     when m.age >= 60 then '경로요금' "+
//                            "     else '일반요금' "+
//                            "end " +
//                    "from Member m";
            //coalesces null이 아니면 값 반환, null일 경우 반환값 병합
            //String query = "select coalesce(m.username, '이름 없는 회원') from Member m ";
            //nullif 두 값이 같으면 null - username이 관리자면 Null 반환
    /*        String query = "select nullif(m.username, '관리자') from Member m ";
            List<String> result = em.createQuery(query, String.class)
                    .getResultList();

            for (String s : result){
                System.out.println("s = " + s);
            }
     */
            //jpql 함수
            //String query = "select 'a' || 'b' from Member m "; //concat('a', 'b')가 더 표준
    /*        String query = "select substring(m.username, 2, 3) from Member m "; //문자열 추출, 자리수 1부터 시작
            //String query = "select function('group_concat', m.username) From Team t"; //사용자 정의 함수
            List<String> result = em.createQuery(query)
                    .getResultList();
            for(String s : result){
                System.out.println("s = " + s);
            }

            //String query2 = "select locate('fg', 'asdfgdqwe') from Member m "; //문자열 추출, 자리수 1부터 시작
            String query2 = "select size(t.members) from Team t "; //size 함수, 컬렉션 크기 반환

            Integer singleResult = em.createQuery(query2, Integer.class).getSingleResult();
            System.out.println("컬렉션 크기 : " + singleResult);

     */
            //경로 표현식
//            String query = "select m.username from Member m";   //상태필드(탐색x)
//            String query = "select m.team.name from Member m";   //단일 값 연관경로 (묵시적 내부 조인 발생, 탐색o)
//            String query = "select t.members from Team t";   //컬랙션 연관경로 (묵시적 내부 조인 발생, 탐색x)
       //     String query = "select m.username from Team t join t.members m";   //컬랙션 연관경로 탐색하려면 명시적 조인을 해야함.
//            List<String> result = em.createQuery(query, String.class)
//                            .getResultList();

/*            List<Collection> result = em.createQuery(query, Collection.class)
                    .getResultList();   //collection은 컬럼 못갖고 옴, 탐색x size 정도만 가져올 수 있음
            for (Object s : result){
                System.out.println("s = " + s);
            }

            Integer singleResult = em.createQuery("select t.members.size from Team t", Integer.class)
                    .getSingleResult();
            System.out.println("t.members.size : " + singleResult);
            // 묵시적 조인 사용 비권장, 매우 위험, 명시적 조인 사용 권장


 */
            //페치 조인
            //String query = "select m From Member m";    // N + 1 문제 발생
            //String query = "select m From Member m join fetch m.team";  // 위 문제 해결 위해 페치조인
            /*List<Member> result = em.createQuery(query, Member.class)
                            .getResultList();
           for(Member members : result) {
                System.out.println("member = " + members.getUsername() + ", " + member.getTeam().getName());
            }

           */

            //String query = "select distinct t From Team t join fetch t.members"; // 컬랙션 페치 조인, 즉시로
            // 1:다 조인이기때문에 데이터 뻥튀기, distinct로 중복 데이터 제거, 단점 별칭이 안됨
            //String query = "select t From Team t join t.members";   //일반 조인 실행시 연관도니 엔티티를 함께 조회하지 않음
            //String query = "select m From Member m join fetch m.team t"; //페이조인 페이징 할때 1:다를 뒤집어 다:1로 쿼리 작성
            String query = "select t From Team t";  //@BatchSize를 같이 사용해 where team_id in ()으로 여러개 같이 조회
            List<Team> result = em.createQuery(query,Team.class)
                    .setFirstResult(0)
                    .setMaxResults(2)     //1:다의 페치조인에서 jpi 페이징 불가, 경고 로그나오는데 절대 쓰면 안됨
                    .getResultList();

            System.out.println("size = " + result.size());
            for(Team team : result) {
                System.out.println("member = " + team.getName() + "|" + team.getMembers().size());
                for (Member members : team.getMembers()){
                    System.out.println("-> member = " + members);
                }
            }

            tx.commit();
        } catch (Exception e){
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}
