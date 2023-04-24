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
            
            .catch(error => console.log(error))
        },

        signUp(){
            axios.post('/api/clients', 'firstName=' + this.firstName + '&lastName=' + this.lastName + '&email=' + this.email + '&password=' + this.password)
            .then(response => {
                console.log('registered');
                this.signIn();
            })
            
            .catch(error => console.log(error))
        }
    }
})

app.mount("#app");