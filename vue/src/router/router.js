import {createRouter, createWebHistory} from 'vue-router'

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
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
                    component: () => import('../components/Home.vue')
                }
            ]
        },
    ]
})
export default router