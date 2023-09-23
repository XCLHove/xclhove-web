<script setup>
import {onMounted, ref, watch} from "vue";
import request from "@/utils/request";
import sleep from "@/utils/sleep";
import config from "@/utils/config";
import WebHead from "@/components/WebHead.vue";
import BeiAnHao from "@/components/BeiAnHao.vue";
import prompt from "@/utils/prompt";
import {useClipboard} from '@vueuse/core'

const data = ref({
  items: [],
  searchText: '',
  isLoad: true,
  /**
   * 获取指定文件夹的子项
   * @param itemId 文件夹id
   */
  requestData: async ({itemId} = {}) => {
    data.value.isLoad = true
    let result
    if (!itemId > 0) {
      result = await request.get('/oneDriveItems')
    } else {
      result = await request.get('/oneDriveItems', {params: {itemId: itemId}})
    }
    await sleep(0.3)
    data.value.isLoad = false
    data.value.items = result.data.oneDriveItems
  },
  /**
   * 搜索文件
   * @Param
   */
  search: async () => {
    request.get(
        `/oneDriveItems/search/${data.value.searchText}`
    ).then(
        result => {
          data.value.items = result.data.oneDriveItems
        }
    ).catch(error => {
      prompt(error)
    })
  },
})
//监听搜索
watch(() => data.value.searchText, () => {
  if (data.value.searchText === '') {
    data.value.items = history.value.records[history.value.index]
    return
  }
  data.value.search()
})

const history = ref({
  index: 0,
  records: [],
  /**
   * 返回根目录
   */
  toRoot: async () => {
    if (history.value.index > 0) {
      history.value.index++
    }
    await data.value.requestData()
    history.value.records[history.value.index] = data.value.items
  },
  /**
   * 后退
   */
  up: () => {
    if (history.value.index > 0) {
      history.value.index--
    }
    data.value.items = history.value.records[history.value.index]
  },
  /**
   * 前进
   */
  down: () => {
    if (history.value.index < history.value.records.length - 1) {
      history.value.index++
    }
    data.value.items = history.value.records[history.value.index]
  },
})
//页面加载完后访问根目录
onMounted(() => {
  history.value.toRoot()
})

const download = ref({
  url: '',
  name: '',
  /**
   * 获取下载链接
   * @param itemId 要下载文件的itemId
   * @return downloadUrl 下载链接
   */
  getUrl: (itemId) => {
    return `${config.serverUrl}/oneDriveItems/download/${itemId}`
  },
  /**
   * 下载文件
   * @param url 下载链接(可选)
   * @param name 下载文件名(可选)
   */
  start: (url = undefined, name = undefined) => {
    let downloadUrl = download.value.url
    let downloadName = download.value.name
    if (url !== undefined) {
      downloadUrl = url
    }
    if (name !== undefined) {
      downloadName = name
    }
    prompt(`开始下载${download.value.name}！`, "success")
    window.location.href = downloadUrl
  },
  copyUrl: () => {
    const {copy} = useClipboard();
    copy(download.value.url);
    prompt("已复制下载链接！", "success")
  }
})

/**
 * 打开文件夹
 * @param itemId
 */
const openFolder = async (itemId) => {
  await data.value.requestData({itemId: itemId})
  history.value.records[++history.value.index] = data.value.items
}

const clickItemIsFile = ref(false)
const itemMenuRef = ref()
const itemMenu = ref({
  isShow: false,
  /**
   * 右键打开菜单
   * @param event 用于获取右键的位置
   * @param item 右键在哪个文件(夹)
   */
  showMenu: (event, item) => {
    itemMenu.value.setPosition(event.pageX, event.pageY)

    clickItemIsFile.value = item.isFile
    download.value.url = download.value.getUrl(item.itemId)
    download.value.name = item.name

    itemMenu.value.isShow = true
  },
  setPosition: (styleLeft, styleTop) => {
    itemMenuRef.value.style.left = `${styleLeft}px`
    itemMenuRef.value.style.top = `${styleTop}px`
  },
})
/**
 * 点击其他地方关闭菜单
 */
document.addEventListener("click", () => {
  itemMenu.value.isShow = false
});
</script>

<template>
  <Load>
    <div class="FileContainer">
      <!-- head -->
      <WebHead>资源</WebHead>
      <!-- file -->
      <div class="file">
        <!-- operation -->
        <div class="operation">
          <span class="part-button">
            <span class="toRoot" @click="history.toRoot()"><i class="iconfont icon-home"></i></span>
            <span class="up" @click="history.up()"><i class="iconfont icon-pageUp"></i></span>
            <span class="down" @click="history.down()"><i class="iconfont icon-pageDown"></i></span>
          </span>
          <span class="part-search">
            <el-input v-model="data.searchText" placeholder="搜索" inline="true"/>
            <el-button type="primary" @click="data.search()">搜索</el-button>
          </span>
        </div>
        <!-- table -->
        <el-table :data="data.items" height="80vh" v-loading="data.isLoad">
          <!-- 文件(夹)名称 -->
          <el-table-column prop="name" label="名称">
            <template #default="items">
              <!-- 文件图标 -->
              <span class="fileIcon">
                <!-- 文件夹 -->
                <i v-if="items.row.isFile === 0" class="iconfont icon-folder"></i>
                <!-- 文本文件 -->
                <i v-else-if="/(\.md)$/gi.test(items.row.name)" class="iconfont icon-markdown"></i>
                <i v-else-if="/(\.txt)$/gi.test(items.row.name)" class="iconfont icon-txt"></i>
                <!-- 压缩包 -->
                <i v-else-if="/(\.zip|\.tar|\.gz)$/gi.test(items.row.name)" class="iconfont icon-zip"></i>
                <i v-else-if="/(\.7z)$/gi.test(items.row.name)" class="iconfont icon-p7z"></i>
                <i v-else-if="/(\.rar)$/gi.test(items.row.name)" class="iconfont icon-rar"></i>
                <!-- 音视频图片 -->
                <i v-else-if="/(\.mp3)$/gi.test(items.row.name)" class="iconfont icon-music"></i>
                <i v-else-if="/(\.mp4)$/gi.test(items.row.name)" class="iconfont icon-video"></i>
                <i v-else-if="/(\.jpg|\.png)$/gi.test(items.row.name)" class="iconfont icon-picture"></i>
                <!-- 可执行程序 -->
                <i v-else-if="/(\.exe)$/gi.test(items.row.name)" class="iconfont icon-exe"></i>
                <i v-else-if="/(\.mis)$/gi.test(items.row.name)" class="iconfont icon-msi"></i>
                <!-- 文档 -->
                <i v-else-if="/(\.ppt|\.pptx)$/gi.test(items.row.name)" class="iconfont icon-ppt"></i>
                <i v-else-if="/(\.doc|\.docx)$/gi.test(items.row.name)" class="iconfont icon-word"></i>
                <i v-else-if="/(\.xls|\.xlsx)$/gi.test(items.row.name)" class="iconfont icon-excel"></i>
                <i v-else-if="/(\.pdf)$/gi.test(items.row.name)" class="iconfont icon-exe"></i>
                <!-- 镜像文件 -->
                <i v-else-if="/(\.iso)$/gi.test(items.row.name)" class="iconfont icon-iso"></i>
                <i v-else-if="/(\.img)$/gi.test(items.row.name)" class="iconfont icon-img"></i>
                <!-- 其它文件 -->
                <i v-else class="iconfont icon-file"></i>
              </span>
              <!-- 文件夹 -->
              <span class="fileName" v-if="items.row.isFile === 0">
                <span @click="openFolder(items.row.id)" @contextmenu.prevent="itemMenu.showMenu($event, items.row)">
                  {{ items.row.name }}
                </span>
              </span>
              <!-- 文件 -->
              <span class="fileName" v-else>
                <a :href="download.getUrl(items.row.itemId)"
                   @contextmenu.prevent="itemMenu.showMenu($event, items.row)"
                >
                  {{ items.row.name }}
                </a>
              </span>
            </template>
          </el-table-column>
          <!-- 文件(夹)大小 -->
          <el-table-column prop="size" label="大小" width="100"/>
        </el-table>
        <!-- itemMenu -->
        <div class="itemMenu" v-show="itemMenu.isShow" ref="itemMenuRef">
          <div class="menuItem borderBottom">新建文件夹</div>
          <div class="menuItem borderBottom">删除</div>
          <div :class="{menuItem:true ,borderBottom:clickItemIsFile}">重命名</div>
          <div class="menuItem borderBottom" v-show="clickItemIsFile" @click="download.copyUrl()">复制下载链接</div>
          <div class="menuItem" v-show="clickItemIsFile" @click="download.start()">下载</div>
        </div>
      </div>
      <!-- 备案号 -->
      <BeiAnHao></BeiAnHao>
    </div>
  </Load>
</template>

<style scoped lang="less">
@import "@/assets/main";

.FileContainer {
  width: 100%;
  height: 100vh;

  .file {
    width: 80%;
    margin: 0 auto;

    .operation {

      .part-button {
        .toRoot, .up, .down {
          margin: 10px;

          i {
            font-size: 20px;
            position: relative;
            top: 5px;
            transition: all 0.2s ease-in-out;
          }

          :hover {
            opacity: 0.5;
          }
        }
      }

      .part-search {
        @media screen and (max-width: 460px) {
          display: block;
          margin: 10px 0;
        }

        * {
          margin: 0 5px;
        }

        .el-input {
          display: inline;

          input {
            width: 500px;
          }
        }

        .el-button {
          padding: 0 10px;
        }
      }
    }

    .fileIcon {
      i {
        margin: 0 5px;
      }
    }

    .fileName {
      a {
        text-decoration: none;
        color: black;
      }
    }

    .itemMenu {
      position: absolute;
      top: 50%;
      left: 50%;
      z-index: 99;
      border: 1px solid #606266;
      border-radius: 5px;
      overflow: hidden;
      background: white;

      .menuItem {
        padding: 5px;
        font-size: 10px;
        color: #606266;
      }

      .menuItem:hover {
        background: #3c3f41;
        color: white;
      }

      .borderBottom {
        border-bottom: 1px solid #606266;
      }
    }
  }
}
</style>