package cn.itcast.core.dao.product;

import cn.itcast.core.bean.product.DataView;
import cn.itcast.core.bean.product.DataViewQuery;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DataViewDao {
    int countByExample(DataViewQuery example);

    int deleteByExample(DataViewQuery example);

    int insert(DataView record);

    int insertSelective(DataView record);

    List<DataView> selectByExample(DataViewQuery example);

    int updateByExampleSelective(@Param("record") DataView record, @Param("example") DataViewQuery example);

    int updateByExample(@Param("record") DataView record, @Param("example") DataViewQuery example);
}