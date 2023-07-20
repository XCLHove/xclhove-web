<template>
  <div class="homeContainer">
    <div class="head">XCLHove</div>
    <div class="gitInfo">
      <a v-for="item in gitInfos" :href="item.url" :key="item.id" target="_blank"><i :class="item.icon"></i></a>
    </div>
    <!--    快速导航    -->
    <div class="fastNavigation">
      <span class="title">快速导航</span>
      <span v-for="item in fastNavigations" :key="item.id">
        <a :href="item.url" target="_blank">{{ item.name }}</a>
      </span>
    </div>
    <!--    搜索框     -->
    <div class="searchInput">
      <el-input v-model="pagination.searchText" placeholder="搜索资源">
        <template #append>
          <el-button :icon="Search" @click="load"/>
        </template>
      </el-input>
    </div>
    <!--    搜索结果    -->
    <div class="searchResult" v-show="links.length > 0">
      <el-table :data="links">
        <el-table-column label="名称" prop="name" width="200" align="center"/>
        <el-table-column label="链接" prop="link">
          <template #default="props">
            <el-link :href="props.row.url" type="primary" target="_blank">立即前往：{{ props.row.name }}</el-link>
          </template>
        </el-table-column>
      </el-table>
      <!--  分页  -->
      <div class="pagination">
        <el-pagination
            v-model:current-page="pagination.pageNumber"
            v-model:page-size="pagination.pageSize"
            :total="total"
            :page-sizes="[5, 10, 15, 30, 50]"
            :small=false
            :disabled=false
            :background=false
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
        />
      </div>
    </div>
  </div>
</template>

<style scoped>
.homeContainer {
  background: #3c3f41;
  color: #42b883;
  width: 100%;
  height: 100vh;
}

.head {
  width: calc(100% - 5px);
  font-size: 50px;
  text-align: center;
  padding: 10px 0;
}

.gitInfo {
  text-align: center;
}

.gitInfo a {
  margin: 0 20px;
  text-decoration: none;
}

.gitInfo a:hover {
  opacity: 0.5;
}

.gitInfo a i {
  font-size: 50px;
  color: var(--color-vue);
}

.fastNavigation {
  margin: 5px 0;
}

.fastNavigation .title {
  font-weight: 700;
}

.fastNavigation span {
  margin: 0 10px;
}

.fastNavigation span a {
  color: #42b883;
  text-decoration: none;
}

.fastNavigation span a:hover {
  text-decoration: underline;
}

.searchResult {
  overflow: hidden;
  border-radius: 5px;
  margin: 5px auto;
}

.searchResult .pagination {
  margin: 5px 0;
}
</style>

<script>
//icon
import {
  Search,
} from "@element-plus/icons-vue";
import request from "@/utils/request";

export default {
  name: 'Home',
  computed: {
    Search() {
      return Search
    },
  },
  data() {
    return {
      gitInfos: [
        {id:1, name: 'github', url: 'https://github.com/xclhove', icon: 'iconfont icon-GitHub'},
        {id:2, name: 'gitee', url: 'https://gitee.com/xclhove', icon: 'iconfont icon-gitee'},
      ],
      fastNavigations: [
        {id: 1, name: '资源站(常用)', url: 'https://zfile.xclhove.top/XCLHove_onedrive_1'},
        {id: 2, name: '资源站(高速)', url: 'https://alist.xclhove.top'},
        {id: 3, name: '资源站(不常用)', url: 'https://zfile.xclhove.top/XCLHove_onedrive_2'},
        {id: 4, name: '网络U盘', url: 'https://zfile.xclhove.top/temp_upload'},
      ],
      links: [],
      pagination: {
        searchText: '',
        pageNumber: 1,
        pageSize: 5,
      },
      total: 4,
    }
  },
  watch: {
    pagination: {
      deep: true,
      immediate: false,
      handler (newValue, oldValue) {
        this.load()
      }
    }
  },
  created() {
    this.load()
  },
  methods: {
    //加载数据
    load() {
      this.links = this.fastNavigations
      this.total = this.fastNavigations.length
      request.get('/links', {
        params: {
          searchText: this.pagination.searchText,
          pageNumber: this.pagination.pageNumber,
          pageSize: this.pagination.pageSize,
        }
      }).then(result => {
        if (result.code === '200') {
          this.links = result.data.links
          this.total = result.data.total
          return
        }
      })
    },
    // 分页-每页数量
    handleSizeChange(pageSize) {
      this.pagination.pageSize = pageSize;
    },
    //分页-页码
    handleCurrentChange(pageNumber) {
      this.pagination.pageNumber = pageNumber;
    },
  },
}
</script>