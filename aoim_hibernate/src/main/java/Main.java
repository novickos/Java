import entity.Classemployee;
import entity.Employee;
import entity.EntityManagerProvider;
import entity.Rate;
import jakarta.persistence.*;
import jakarta.persistence.criteria.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Main {

    public static void exportToCSV(List<Employee> employees) {
        String filePath = "employees.csv";

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.append("ID,Name,Surname,Cond,BirthYear,Salary,GroupName\n");

            for (Employee employee : employees) {
                writer.append(String.valueOf(employee.getId())).append(",")
                        .append(employee.getName()).append(",")
                        .append(employee.getSurname()).append(",")
                        .append(employee.getCond()).append(",")
                        .append(String.valueOf(employee.getBirthYear())).append(",")
                        .append(String.valueOf(employee.getSalary())).append(",")
                        .append(employee.getClassEmployee().getGroupName()).append("\n");
            }

            System.out.println("Dane zostały zapisane do pliku: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("aoim_hibernate");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();



            //select all employees
            TypedQuery<Employee> allEmployeesQuery = entityManager.createNamedQuery("Employee.findAll", Employee.class);
            for (Employee employee : allEmployeesQuery.getResultList()) {
                System.out.println(employee);
            }

            System.out.println("RATATATATATTATATATA");
            //select all classemp
            TypedQuery<Classemployee> allClassesQuery = entityManager.createNamedQuery("ClassEmployee.findAll", Classemployee.class);
            for (Classemployee classEmp : allClassesQuery.getResultList()) {
                System.out.println(classEmp);
            }


            //wypisz konkretna grupe
            TypedQuery<Classemployee> classByGroupQuery = entityManager.createNamedQuery("ClassEmployee.findByGroupName", Classemployee.class);
            classByGroupQuery.setParameter(1, "jojo jojo");
            for (Classemployee classemp : classByGroupQuery.getResultList()) {
                System.out.println(classemp);
            }
            System.out.println("RATATATATATTATATATA");


            //employee by class group
            TypedQuery<Employee> empByClassQuery = entityManager.createNamedQuery("Employee.byClass", Employee.class);
            empByClassQuery.setParameter(1, "twojastara");
            for (Employee employee : empByClassQuery.getResultList()) {
                System.out.println(employee);

            }

            //employee count by class
           Query countEmpByClass = entityManager.createNativeQuery("select count(*) from employee inner join aoim_4.classemployee c on employee.classEmployee_id = c.id where c.groupName=:className");
            countEmpByClass.setParameter("className", "twojastara");
            System.out.println("There r " + countEmpByClass.getSingleResult()+" ppl here");


//            usun ziomeczka
//            Employee employeeToDelete = entityManager.find(Employee.class, 25L); //{x}L to ID do usuniecia jbc
//            if (employeeToDelete != null) {
//                entityManager.remove(employeeToDelete);
//                System.out.println("Deleted employee " + employeeToDelete);
//            }

            //dodaj ziomeczka
//            Employee emp = new Employee();
//            emp.setName("Joe");
//            emp.setSurname("Keery");
//            emp.setBirthYear(1990);
//            emp.setSalary(12345.45);
//            emp.setCond("Active");
//
//            if (emp.getId() == null) {
//                entityManager.persist(emp);
//            } else {
//                entityManager.merge(emp);
//            }

            //dodaj grupe
//            Classemployee cemp = new Classemployee();
//            cemp.setGroupName("twojastara123");
//            cemp.setMaxNumber(10);
//
//            if(cemp.getId() == null) {
//                entityManager.persist(cemp);
//            } else {
//                entityManager.merge(cemp);
//            }

            //gettin rid of grupa :ccc
//            Classemployee classToDelete = entityManager.find(Classemployee.class, 1L); //ta sama sprawa jak z emp do usuniecia, 1L to id
//            if (classToDelete != null) {
//                TypedQuery<Employee> deleteEmployeesQuery = entityManager.createQuery(
//                        "SELECT e FROM Employee e WHERE e.classEmployee = :classEmployee", Employee.class);
//                deleteEmployeesQuery.setParameter("classEmployee", classToDelete);
//                List<Employee> employeesToDelete = deleteEmployeesQuery.getResultList();
//
//                // wszyscy pracownicy sio
//                for (Employee employee : employeesToDelete) {
//                    entityManager.remove(employee);
//                }
//                // sayonara grupo
//                entityManager.remove(classToDelete);
//            }

            TypedQuery<Employee> sortedBySurnameQuery = entityManager.createQuery("SELECT e FROM Employee e ORDER BY e.surname ASC", Employee.class);
            for (Employee employee : sortedBySurnameQuery.getResultList()) {
                System.out.println(employee);
            }

            TypedQuery<Employee> sortedBySurnamePartQuery = entityManager.createQuery(
                    "SELECT e FROM Employee e WHERE e.surname LIKE :surnamePart ORDER BY e.surname ASC", Employee.class);
            sortedBySurnamePartQuery.setParameter("surnamePart", "%k%");
            for (Employee employee : sortedBySurnamePartQuery.getResultList()) {
                System.out.println(employee);
            }

//POBIERANIE I WGL DO CSV
            EntityManager entityManagerProv = EntityManagerProvider.getEntityManager();

            String hql = "FROM Employee e";
            TypedQuery<Employee> query = entityManagerProv.createQuery(hql, Employee.class);

            List<Employee> employees = query.getResultList();

            exportToCSV(employees); //TUTAJ ODKOMENTUJ JBC

            entityManagerProv.close();
            EntityManagerProvider.close();


//////////////////////////////////////////////////////////
            //Criteria
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);

            Root<Rate> rateRoot = criteriaQuery.from(Rate.class);

            Join<Rate, Classemployee> classEmployeeJoin = rateRoot.join("classEmployee");

            criteriaQuery.multiselect(classEmployeeJoin.get("groupName"), criteriaBuilder.count(rateRoot), criteriaBuilder.avg(rateRoot.get("value")));
            criteriaQuery.groupBy(classEmployeeJoin.get("groupName"));

            List<Object[]> results = entityManager.createQuery(criteriaQuery).getResultList();

            for (Object[] result : results) {
                String groupName = (String) result[0];
                Long rateCount = (Long) result[1];
                Double averageRate = (Double) result[2];

                System.out.println("Group: " + groupName + ", Number of Rates: " + rateCount + ", Average Rate: " + averageRate);
            }

//mega
            transaction.commit();
        } finally {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            entityManager.close();
            entityManagerFactory.close();
        }
    }
}
