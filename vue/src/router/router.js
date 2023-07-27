import {createRouter, createWebHashHistory, createWebHistory} from 'vue-router'

const router = createRouter({
    // history: createWebHistory(import.meta.env.BASE_URL),
    history: createWebHashHistory(import.meta.env.BASE_URL),
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