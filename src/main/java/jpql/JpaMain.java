package jpql;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

public class JpaMain {

    public static void main(String[] args){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);
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

            Member singleResult = em.createQuery("select m from Member m where m.username = :username", Member.class)
                    .setParameter("username", "member1")
                    .getSingleResult();
            System.out.println("singleResult : " + singleResult);


            tx.commit();
        } catch (Exception e){
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}
