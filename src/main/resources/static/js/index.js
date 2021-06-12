let Login = Vue.extend({

    template: ``
})

const router = new VueRouter({
    history:false,
    routes: [
        // 动态路径参数 以冒号开头
        {path: '/login', component: Login}
    ]
})
window.onload = function () {
    new Vue({
        el: "#app",
        data: {
            name: "Hello"
        },
        methods: {},
        router
    })
}

