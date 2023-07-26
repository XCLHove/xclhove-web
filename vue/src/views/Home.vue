<script setup>
//icon
import {
  Search,
} from "@element-plus/icons-vue";
//组件
import Head from '@/components/Head.vue';
import request from "@/utils/request";
import router from "@/router/router";
import {ref, watch} from "vue";

const gitInfos = ref([
  {id: 1, name: 'github', url: 'https://github.com/xclhove', icon: 'iconfont icon-GitHub'},
  {id: 2, name: 'gitee', url: 'https://gitee.com/xclhove', icon: 'iconfont icon-gitee'},
])

const fastNavigations = ref([
  {id: 1, name: '资源站(常用)', url: 'https://zfile.xclhove.top/XCLHove_onedrive_1'},
  {id: 2, name: '资源站(高速)', url: 'https://alist.xclhove.top'},
  {id: 3, name: '资源站(不常用)', url: 'https://zfile.xclhove.top/XCLHove_onedrive_2'},
  {id: 4, name: '网络U盘', url: 'https://zfile.xclhove.top/upload before'},
])
const loadFastNavigations = () => {
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
    fastNavigations.value = result.data.links
  })
}

const links = ref([])
const init = () => {
  links.value = fastNavigations.value
  total.value = fastNavigations.value.length
}
const load = () => {
  searchResultLoading.value = true
  request.get('/links', {
    params: {
      searchText: searchText.value,
      pageNumber: pageNumber.value,
      pageSize: pageSize.value,
    }
  }).then(result => {
    if (result.code === '200') {
      links.value = result.data.links
      total.value = result.data.total
      searchResultLoading.value = false
    }
  })
}

const searchText = ref('')
const pageNumber = ref(1)
const pageSize = ref(5)
const total = ref(0)
const searchResultLoading = ref(false)
//分页-每页数量
const handleSizeChange = (newPageSize) => {
  pageSize.value = newPageSize
}
//分页-页码
const handleCurrentChange = (newPageNumber) => {
  pageNumber.value = newPageNumber
}
watch([
  searchText,
  pageNumber,
  pageSize,
], () => {
  load()
})

init()
loadFastNavigations()
load()
</script>

<template>
  <!--  首页  -->
  <div class="homeContainer">
    <!--  head  -->
    <Head>XCLHove</Head>
    <!--  后台管理  -->
    <div class="backLogin">
      <router-link to="/backLogin"><i class="iconfont icon-login"></i></router-link>
    </div>
    <!--  gitInfo -->
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
      <el-input v-model="searchText" placeholder="搜索资源(支持关键字)">
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
            v-model:current-page="pageNumber"
            v-model:page-size="pageSize"
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
    border: var(--color-lightGray) solid 1px;

    .pagination {
      margin: 5px;
    }
  }
}
</style>