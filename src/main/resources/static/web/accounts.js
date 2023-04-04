console.log("Hola");
const {createApp} = Vue;

const app = createApp({
    data(){
        return{
            datos: [],
            firstName: "",
            lastName: "",
        }
    },

    created(){
        this.loadData();
    },

    methods:{
        loadData(){
            axios.get('/api/clients/1')
            .then(response => {
                this.datos = response.data;
                console.log(this.datos);
            })
            .catch(error => console.log(error))
        },

        addClient(){
            this.postClient();
        },

        postClient(){
            axios.post('/api/clients/1',{
                firstName: this.firstName,
                lastName: this.lastName,          
            })
            .then(function(response){
                this.loadData();
            })
            .catch(error => console.log(error))
        }
    }

})

app.mount("#app");