package gr.aueb.cf.firstapp;

import gr.aueb.cf.firstapp.model.Teacher;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import java.util.List;

public class App 
{
    public static void main( String[] args )
    {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("teachersPU");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();

        Teacher teacher1 = new Teacher();
        teacher1.setFirstname("Athanassios");
        teacher1.setLastname("Androutsos");

        Teacher teacher2 = new Teacher();
        teacher2.setFirstname("Makis");
        teacher2.setLastname("Kapetis");

        Teacher teacher3 = new Teacher();
        teacher3.setFirstname("Anna");
        teacher3.setLastname("Giannoutsos");

        // Insert
        em.persist(teacher1);
        em.persist(teacher2);
        em.persist(teacher3);
        em.getTransaction().commit();

        // Update
        em.getTransaction().begin();
        Teacher newTeacher = new Teacher();
        newTeacher.setId(2);
        newTeacher.setFirstname("Chrysostomos");
        newTeacher.setLastname("Kap.");
        em.merge(newTeacher);
        em.getTransaction().commit();

        // Get by id
        em.getTransaction().begin();
        Teacher athanassios = em.find(Teacher.class, 1);
        System.out.println(athanassios);
        em.getTransaction().commit();

        // Delete
        em.getTransaction().begin();
        Teacher teacher = em.find(Teacher.class, 2);
        em.remove(teacher);
        em.getTransaction().commit();

        // Get All with a raw query
        em.getTransaction().begin();
        TypedQuery<Teacher> teacherTypedQuery = em.createQuery("SELECT t FROM Teacher t", Teacher.class);
        List<Teacher> teachers = teacherTypedQuery.getResultList();
        em.getTransaction().commit();

        teachers.forEach(System.out::println);

        // Get by firstname with a raw parameterized query
        em.getTransaction().begin();
        TypedQuery<Teacher> teacherNamedAthanassios = em.createQuery("SELECT t FROM Teacher t WHERE t.firstname =  :name", Teacher.class);
        teacherNamedAthanassios.setParameter("name", "Athanassios");
        Teacher athana = teacherNamedAthanassios.getSingleResult();
        em.getTransaction().commit();
        System.out.println(athana);

        // Get number of rows with a raw query
        em.getTransaction().begin();
        Query countQuery = em.createQuery("SELECT COUNT(*) FROM Teacher");
        long result = (long) countQuery.getSingleResult();
        em.getTransaction().commit();
        System.out.println("Teacher Entities Count: " + result);


        // Get All with a CriteriaBuilder
        em.getTransaction().begin();
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Teacher> criteriaQuery = builder.createQuery(Teacher.class);
        Root<Teacher> root = criteriaQuery.from(Teacher.class);
        // Get one row
//        criteriaQuery.select(root).where(builder.equal(root.get("firstname"), "Athanassios"));
        criteriaQuery.select(root);

        TypedQuery<Teacher> query = em.createQuery(criteriaQuery);
        List<Teacher> teacherList = query.getResultList();
        em.getTransaction().commit();

        teacherList.forEach(System.out::println);

        // Get filtered with a CriteriaBuilder and parameters (where)
        em.getTransaction().begin();
        CriteriaBuilder builder1 = em.getCriteriaBuilder();
        CriteriaQuery<Teacher> criteriaQuery1 = builder1.createQuery(Teacher.class);
        Root<Teacher> root1 = criteriaQuery1.from(Teacher.class);
        ParameterExpression<String> firstnameParam = builder1.parameter(String.class, "teacherFirstname");
        criteriaQuery1.select(root1).where(builder1.equal(root1.get("firstname"), firstnameParam));

        TypedQuery<Teacher> query1 = em.createQuery(criteriaQuery1);
        query1.setParameter("teacherFirstname", "Athanassios");
        List<Teacher> teacherList1 = query1.getResultList();
        em.getTransaction().commit();

        teacherList1.forEach(System.out::println);

        // Get filtered with a CriteriaBuilder and parameters (like)
        em.getTransaction().begin();
        CriteriaBuilder builder2 = em.getCriteriaBuilder();
        CriteriaQuery<Teacher> criteriaQuery2 = builder2.createQuery(Teacher.class);
        Root<Teacher> root2 = criteriaQuery2.from(Teacher.class);
        ParameterExpression<String> lastnameParam = builder2.parameter(String.class, "teacherLastname");
        criteriaQuery2.select(root2).where(builder2.like(root2.get("lastname"), lastnameParam));

        TypedQuery<Teacher> query2 = em.createQuery(criteriaQuery2);
        query2.setParameter("teacherLastname", "Giannou%");
        List<Teacher> teacherList2 = query2.getResultList();
        em.getTransaction().commit();

        teacherList2.forEach(System.out::println);

        // Get filtered with a CriteriaBuilder and parameters (and)
        em.getTransaction().begin();
        CriteriaBuilder builder3 = em.getCriteriaBuilder();
        CriteriaQuery<Teacher> criteriaQuery3 = builder3.createQuery(Teacher.class);
        Root<Teacher> root3 = criteriaQuery3.from(Teacher.class);
        criteriaQuery3.select(root3).where(builder3.and(builder3.equal(root3.get("firstname"), "Athanassios")),builder3.like(root3.get("lastname"), "Androu%"));

        TypedQuery<Teacher> query3 = em.createQuery(criteriaQuery3);
        List<Teacher> teacherList3 = query3.getResultList();
        em.getTransaction().commit();

        teacherList3.forEach(System.out::println);

        em.close();
        emf.close();
    }
}
