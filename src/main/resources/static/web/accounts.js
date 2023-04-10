console.log("Hola");
const {createApp} = Vue;

const app = createApp({
    data(){
        return{
            datos: [],
            firstName: "",
            lastName: "",
            // id: (new URLSearchParams(location.search)).get("id"),
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
    }

})

app.mount("#app");