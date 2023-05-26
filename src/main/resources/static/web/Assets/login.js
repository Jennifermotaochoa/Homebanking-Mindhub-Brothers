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
            .then(response => {
                if(this.email == "jennifer@hotmail.com"){
                    window.location.href = "/manager.html"
                }
                else{
                    window.location.href = "/web/accounts.html"
                }
                })
            
            .catch(error => Swal.fire({
                title: 'Error',
                text: 'Your password or email is incorrect',
                icon: 'error'
            }))
        }
    }
})

app.mount("#app");