import {createRouter, createWebHashHistory, createWebHistory} from 'vue-router'

const router = createRouter({
    // history: createWebHistory(import.meta.env.BASE_URL),
    history: createWebHashHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/',
            name: 'layout',
            redirect: 'home',
            component: () => import('../layout/Layout.vue'),
            children: [
                {
                    path: 'home',
                    name: 'home',
                    component: () => import('../views/Home.vue')
                }
            ]
        },
    ]
})
export default router