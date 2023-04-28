console.log("Hola");
const {createApp} = Vue;

const app = createApp({
    data(){
        return{
            datos: [],
            firstName: "",
            lastName: "",
            loans:[],
        }
    },

    created(){
        this.loadData()    
    },

    methods:{
        loadData(){
            axios.get('/api/clients/current')
            .then(response => {
                this.datos = response.data;
                console.log(this.datos);
                this.loans = this.datos.loans;
                console.log(this.loans);
            })
            .catch(error => console.log(error))
        },

        newAccount(){
            axios.post('/api/clients/current/accounts')
            .then(response => 
                window.location.href="/web/accounts.html")
                
            .catch(error => Swal.fire({
                title: 'Error',
                text: error.response.data,
                icon: 'error'
            }))
        },

        logout(){
            axios.post('/api/logout')
            .then(response => 
                window.location.href="/web/index.html")
            .catch(error => console.log(error))
        },
    }

})

app.mount("#app");