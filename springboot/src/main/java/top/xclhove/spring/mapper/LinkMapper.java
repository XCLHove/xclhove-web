package top.xclhove.spring.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import top.xclhove.spring.entity.Link;

import java.util.List;

@Mapper
public interface LinkMapper {
    @Select("select * from link where id in ( " +
            "select distinct link_id " +
            "from keyword " +
            "where keyword like concat('%', #{searchText} ,'%')) " +
            "limit #{pageNumber}, #{pageSize}")
    List<Link> search(@Param("searchText") String searchText,
                      @Param("pageNumber") Integer pageNumber,
                      @Param("pageSize") Integer pageSize);
    @Select("select count(*) from link where id in ( " +
            "select distinct link_id " +
            "from keyword " +
            "where keyword like concat('%', #{searchText} ,'%')) ")
    int searchTotal(@Param("searchText") String searchText);
}
