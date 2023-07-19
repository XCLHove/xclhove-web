<template>
  <div class="homeContainer">
    <div class="head">XCLHove</div>
    <div class="gitInfo">
      <a v-for="item in gitInfos" :href="item.link" target="_blank"><i :class="item.icon"></i></a>
    </div>
    <!--    快速导航    -->
    <div class="fastNavigation">
      <span class="title">快速导航</span>
      <span v-for="item in fastNavigations">
        <a :href="item.link" target="_blank">{{ item.name }}</a>
      </span>
    </div>
    <!--    搜索框     -->
    <div class="searchInput">
        <el-input v-model="searchText" placeholder="搜索资源">
          <template #append><el-button :icon="Search" @click="load"/></template>
        </el-input>
    </div>
    <!--    搜索结果    -->
    <div class="searchResult" v-show="links.length > 0">
      <el-table :data="links">
        <el-table-column label="名称" prop="name" width="200" align="center"/>
        <el-table-column label="链接" prop="link">
          <template #default="props">
            <el-link :href="props.row.link" type="primary" target="_blank">立即前往：{{ props.row.name }}</el-link>
          </template>
        </el-table-column>
      </el-table>
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
</style>

<script>
//icon
import {
  Search,
} from "@element-plus/icons-vue";
//消息提示
import prompt from "@/utils/prompt"

export default {
  name: "Home",
  computed: {
    Search() {
      return Search
    },
  },
  data() {
    return {
      gitInfos: [
        {name:'github', link: 'https://github.com/xclhove', icon: 'iconfont icon-GitHub'},
        {name:'gitee', link: 'https://gitee.com/xclhove', icon: 'iconfont icon-gitee'},
      ],
      fastNavigations: [
        {name: '资源站(常用)', link: "https://zfile.xclhove.top/XCLHove_onedrive_1"},
        {name: '资源站(高速)', link: "https://alist.xclhove.top"},
        {name: '资源站(不常用)', link: "https://zfile.xclhove.top/XCLHove_onedrive_2"},
        {name: '网络U盘', link: "https://zfile.xclhove.top/temp_upload"},
      ],
      links: [
        {name: '资源站(常用)', link: "https://zfile.xclhove.top/XCLHove_onedrive_1"},
        {name: '资源站(高速)', link: "https://alist.xclhove.top"},
        {name: '资源站(不常用)', link: "https://zfile.xclhove.top/XCLHove_onedrive_2"},
        {name: '网络U盘', link: "https://zfile.xclhove.top/temp_upload"},
      ],
      searchText: '',
      total: 4,
      pageSize: 5,
      pageNumber: 1,
    }
  },
  methods: {
    //加载数据
    load() {
      prompt('暂未开放！', 'success')
    },
    // 分页-每页数量
    handleSizeChange(pageSize) {
      this.pageSize = pageSize;
    },
    //分页-页码
    handleCurrentChange(pageNumber) {
      this.pageNumber = pageNumber;
    },
  },
}
</script>