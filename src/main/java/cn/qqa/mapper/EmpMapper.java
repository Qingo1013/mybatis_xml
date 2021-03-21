package cn.qqa.mapper;

import cn.qqa.pojo.Emp;

/**
 * 注意
 * xxxMapper.xml文件中，同一个命名空间只能有一个唯一的id,
 * 所以在接口中也只能有唯一一个方法名，虽然java中可以函数重载，但是mybatis不支持
 */
public interface EmpMapper {

    //查询
    Emp selectEmp(Integer id);
    //插入
    Integer insertEmp(Emp emp);

    //更新
    Integer updateEmp(Emp emp);

    /**
     * 赠删改的返回值 除了可以声明int(Integer)还可以声明bool(Boolean) 如果大于1就返回ture
     * @param id
     * @return
     */
    //删除
    Boolean deleteEmp(Integer id);
}
