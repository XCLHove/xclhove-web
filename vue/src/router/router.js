import {createRouter, createWebHashHistory, createWebHistory} from 'vue-router'

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    // history: createWebHashHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/',
            name: 'layout',
            redirect: 'home',
            component: () => import('@/layout/Layout.vue'),
            children: [
                {
                    path: 'home',
                    name: 'home',
                    component: () => import('@/views/Home.vue')
                }
            ]
        },
        {
            path: '/init',
            name: 'init',
            component: () => import('@/views/Init.vue'),
        },
        {
            path: '/backLogin',
            name: 'backLogin',
            component: () => import('@/views/BackLogin.vue'),
        },
        {
            path: '/drive',
            name: 'drive',
            component: () => import('@/views/Drive.vue'),
        },
        {
            path: '/admin',
            name: 'admin',
            component: () => import('@/views/Admin.vue'),
        },
        {
            path: '/addStorage',
            name: 'addStorage',
            component: () => import('@/views/AddStorage.vue'),
        },
        {
            path: '/callback',
            name: 'callback',
            component: () => import('@/views/Callback.vue'),
        },
        {
            path: '/404',
            name: '404',
            component: () => import('@/views/404.vue'),
        },
        {
            path: '/:catchAll(.*)',
            redirect: '/404'
        }
    ]
})
export default router