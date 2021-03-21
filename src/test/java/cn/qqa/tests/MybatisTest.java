package cn.qqa.tests;

import cn.qqa.mapper.EmpMapper;
import cn.qqa.pojo.Emp;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

/**
 * 日志配置
 * 1.导入pom依赖
 * 2.添加logback配置文件
 * 3.在某个类下加入声明
 */
public class MybatisTest {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    SqlSessionFactory sqlSessionFactory;
    @Before
    public void before(){
        //从XML中构建SqlSeesionFactory
        String resource = "mybatis-config.xml";
        InputStream inputStream = null;
        try {
            inputStream = Resources.getResourceAsStream(resource);
        }catch (IOException e){
            e.printStackTrace();
        }
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
    }


    @Test
    public void test01() throws IOException, SQLException {
        //根据mybatis-config.xml全局配置文件构建SqlSessionFactory
        String config = "mybatis-config.xml";
        //将xml构建成输入流，因为SqlSessionFactory只接受输入流
        InputStream inputStream = Resources.getResourceAsStream(config);
        //新建SqlSessionFactory工厂,是比较重量级的：将全局配置文件以及所有的mapper全部加载到Configuration对象中
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        //SqlSession负责执行具体的数据库操作
        /**
         *
         *  给openSession传入不同的参数会给SqlSession后续的数据库操作造成不同的影响
         *  默认的 openSession() 方法没有参数，它会创建具备如下特性的 SqlSession：
         *  事务作用域将会开启（也就是不自动提交）。
         *  将由当前环境配置的 DataSource 实例中获取 Connection 对象。
         *  事务隔离级别将会使用驱动或数据源的默认设置。
         *  预处理语句不会被复用，也不会批量处理更新。
         *
         *  参数：
         *  boolean autoCommit：可以设置事务为自动提交，否则需要手动提交
         *  Connection connection：可以动态切换连接
         *  TransactionIsolationLevel level：可以设置事务隔离级别
         *  ExecutorType exectype：预处理语句（就是sql语句）是否复用，是否批量处理更新
         *
         *  ExecutorType.SIMPLE：该类型的执行器没有特别的行为。它为每个语句的执行创建一个新的预处理语句。
         *  ExecutorType.REUSE：该类型的执行器会复用预处理语句。
         *  ExecutorType.BATCH：该类型的执行器会批量执行所有更新语句，如果 SELECT 在多个更新中间执行，将在必要时将多条更新语句分隔开来，以方便理解。
         */
        SqlSession sqlSession = sqlSessionFactory.openSession();

        String databaseProductName = sqlSession.getConnection().getMetaData().getDatabaseProductName();
        System.out.println(databaseProductName);

        //EmpMapper是接口，为什么能被实例化？
        //Mybatis在调用getMapper时，底层会创建jdk动态代理
        EmpMapper mapper = sqlSession.getMapper(EmpMapper.class);
        try{
            Emp emp = mapper.selectEmp(1);
            System.out.println(emp);
        }catch (Exception ex){

            System.out.println("查询失败！");
        }
        sqlSession.commit();
        sqlSession.close();
    }

    /**
     * 日志级别
     * TRACE < DEBUG < INFO < WARN < ERROR
     *  1      2       3     4       5
     *  设置为2时，大于等于2的都会输出
     */
    @Test
    public void test02(){
        logger.trace("跟踪级别");
        logger.debug("调试级别");
        logger.info("信息级别");
        logger.warn("警告级别");
        logger.error("异常级别");
    }

    /**
     * 插入
     * 注意要提交事务
     */
    @Test
    public void insert(){
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        EmpMapper mapper = sqlSession.getMapper(EmpMapper.class);
        Emp emp = new Emp();
        emp.setUsername("汪星源");
        try{
            Integer result = mapper.insertEmp(emp);
            System.out.println(result);
            System.out.println(emp);
            sqlSession.commit();
        }catch (Exception ex){
            System.out.println("插入失败");
            sqlSession.rollback();;
        }finally {
            sqlSession.close();
        }
    }

    /**
     * 更新
     * 注意要提交事务
     */
    @Test
    public void update(){
        SqlSession sqlSession = sqlSessionFactory.openSession();
        EmpMapper mapper = sqlSession.getMapper(EmpMapper.class);
        Emp emp = new Emp();
        emp.setId(5);
        emp.setUsername("汪星源");
        try{
            Integer result = mapper.updateEmp(emp);
            System.out.println(result);
            sqlSession.commit();
        }catch (Exception ex){
            System.out.println("插入失败");
            sqlSession.rollback();;
        }finally {
            sqlSession.close();
        }
    }

    /**
     * 删除
     * 注意要提交事务
     */
    @Test
    public void delete(){
        SqlSession sqlSession = sqlSessionFactory.openSession();
        EmpMapper mapper = sqlSession.getMapper(EmpMapper.class);
        try{
            boolean result = mapper.deleteEmp(7);
            System.out.println(result);
            sqlSession.commit();
        }catch (Exception ex){
            System.out.println("删除失败");
            sqlSession.rollback();;
        }finally {
            sqlSession.close();
        }
    }


}
