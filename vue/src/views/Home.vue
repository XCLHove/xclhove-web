<script>
//icon
import {
  Search,
} from "@element-plus/icons-vue";
import request from "@/utils/request";
import router from "@/router/router";

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
        {id: 1, name: 'github', url: 'https://github.com/xclhove', icon: 'iconfont icon-GitHub'},
        {id: 2, name: 'gitee', url: 'https://gitee.com/xclhove', icon: 'iconfont icon-gitee'},
      ],
      fastNavigations: [
        {id: 1, name: '资源站(常用)', url: 'https://zfile.xclhove.top/XCLHove_onedrive_1'},
        {id: 2, name: '资源站(高速)', url: 'https://alist.xclhove.top'},
        {id: 3, name: '资源站(不常用)', url: 'https://zfile.xclhove.top/XCLHove_onedrive_2'},
        {id: 4, name: '网络U盘', url: 'https://zfile.xclhove.top/upload before'},
      ],
      links: [],
      pagination: {
        searchText: '',
        pageNumber: 1,
        pageSize: 5,
      },
      total: 4,
      searchResultLoading: false,
    }
  },
  watch: {
    pagination: {
      deep: true,
      immediate: false,
      handler(newValue, oldValue) {
        this.load()
      }
    }
  },
  created() {
    this.load()
    this.loadFastNavigations()
  },
  methods: {
    router() {
      return router
    },
    //加载数据
    load() {
      this.searchResultLoading = true
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
          this.searchResultLoading = false
        }
      })
    },
    loadFastNavigations() {
      request.get('/links', {
        params: {
          searchText: 'fastNavigations',
          pageNumber: 1,
          pageSize: 10,
        }
      }).then(result => {
        if (result.code !== '200') {
          return
        }
        if (result.data.links.length <= 0) {
          return
        }
        this.fastNavigations = result.data.links
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

<template>
  <!--  首页  -->
  <div class="homeContainer">
    <!--  head  -->
    <div class="head">XCLHove</div>
    <div class="gitInfo">
      <a v-for="item in gitInfos" :href="item.url" :key="item.id" target="_blank"><i :class="item.icon"></i></a>
    </div>
    <!--  快速导航  -->
    <div class="fastNavigation">
      <span class="title">快速导航</span>
      <span v-for="item in fastNavigations" :key="item.id">
        <a :href="item.url" target="_blank">{{ item.name }}</a>
      </span>
    </div>
    <!--  搜索框 -->
    <div class="searchInput">
      <el-input v-model="pagination.searchText" placeholder="搜索资源(支持关键字)">
        <template #append>
          <el-button :icon="Search" @click="load">
            <span class="buttonText">搜索</span>
          </el-button>
        </template>
      </el-input>
    </div>
    <!--  搜索结果  -->
    <div class="searchResult" v-show="links.length > 0" v-loading="searchResultLoading">
      <el-table :data="links">
        <el-table-column label="名称" prop="name" align="center" show-overflow-tooltip/>
        <el-table-column label="链接" prop="link" align="center" show-overflow-tooltip>
          <template #default="links">
            <el-link :href="links.row.url" type="primary" target="_blank">立即前往：{{ links.row.name }}</el-link>
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

<style lang="less" scoped>
@import "@/assets/main";

.homeContainer {
  background: white;
  color: var(--color-gray);
  width: 100%;
  height: 100vh;
  padding: 0 20px;
  .border-box();

  .head {
    width: 100%;
    font-size: 50px;
    text-align: center;
    padding: 10px 0;
  }

  .gitInfo {
    text-align: center;

    a {
      margin: 0 20px;
      text-decoration: none;

      &:hover {
        opacity: 0.5;
      }

      i {
        font-size: 50px;
        color: var(--color-gray);
      }
    }
  }

  .fastNavigation {
    margin: 5px 0;
    color: #42b883;

    .title {
      font-weight: 700;
    }

    span {
      margin-right: 10px;

      a {
        color: #42b883;
        text-decoration: none;

        &:hover {
          text-decoration: underline;
        }
      }
    }
  }

  .searchInput {

    button {
      padding: 0 10px;

      .buttonText {
        position: relative;
        bottom: 2px;
      }
    }
  }

  .searchResult {
    overflow: hidden;
    border-radius: 5px;
    margin: 5px auto;
    .border-box();
    border: #dcdfe6 solid 1px;

    .pagination {
      margin: 5px;
    }
  }
}
</style>