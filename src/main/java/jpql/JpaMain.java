package jpql;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
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

            Member member = new Member();
            member.setUsername("member0");
            member.setAge(100);
            em.persist(member);

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
            // 페이징

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

            tx.commit();
        } catch (Exception e){
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}
