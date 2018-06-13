package cn.itcast.core.dao.product;

import cn.itcast.core.bean.product.Data;
import cn.itcast.core.bean.product.DataQuery;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DataDao {
    int countByExample(DataQuery example);

    int deleteByExample(DataQuery example);

    int deleteByPrimaryKey(Long id);

    int insert(Data record);

    int insertSelective(Data record);

    List<Data> selectByExample(DataQuery example);

    Data selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Data record, @Param("example") DataQuery example);

    int updateByExample(@Param("record") Data record, @Param("example") DataQuery example);

    int updateByPrimaryKeySelective(Data record);

    int updateByPrimaryKey(Data record);
    
    public void deletes(Long[] ids);
}