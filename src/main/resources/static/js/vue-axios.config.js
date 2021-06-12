const request = axios.create({
    baseURL:'http://localhost:8081/',
})

request.interceptors.request.use(config=>{
    return config;
},err=>{
    return Promise.reject(err)
})


request.interceptors.response.use(response=>{
    return response;
},err=>{
    return Promise.reject(err);
})

Vue.prototype.$axios = request;