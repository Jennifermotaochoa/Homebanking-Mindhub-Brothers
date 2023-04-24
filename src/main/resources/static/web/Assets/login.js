console.log("Hola");

const {createApp} = Vue;

const app = createApp({
    data(){
        return{
            email: "",
            password: "",
        }

    },

    methods:{
        signIn(){
            axios.post('/api/login','email=' + this.email + '&password=' + this.password)
            .then(response => { window.location.href = "/web/accounts.html"})
            
            .catch(error => console.log(error))
        }
    }
})

app.mount("#app");