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
        this.loadData();
    },

    methods:{
        loadData(){
            axios.get('/api/clients/1')
            .then(response => {
                this.datos = response.data;
                console.log(this.datos);
                this.loans = this.datos.loans;
                console.log(this.loans);
            })
            .catch(error => console.log(error))
        },
    }

})

app.mount("#app");