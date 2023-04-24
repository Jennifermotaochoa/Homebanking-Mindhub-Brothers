console.log("Hola");

const {createApp} = Vue;

const app = createApp({
    data(){
        return{
            firstName:"",
            lastName:"",
            email: "",
            password: "",
        }

    },

    methods:{
        signIn(){
            axios.post('/api/login','email=' + this.email + '&password=' + this.password)
            .then(response => { window.location.href = "/web/accounts.html"})
            
            .catch(error => 
                Swal.fire({
                    title: 'Error',
                    text: 'Your password or email is incorrect',
                    icon: 'error'
                }))
        },

        signUp(){
            axios.post('/api/clients', 'firstName=' + this.firstName + '&lastName=' + this.lastName + '&email=' + this.email + '&password=' + this.password)
            .then(response => {
                console.log('registered');
                this.signIn();
            })
            
            .catch(error => Swal.fire({
                title: 'Error',
                text: error.response.data,
                icon: 'error'
            }))
        }
    }
})

app.mount("#app");