console.log("Hola");
const {createApp} = Vue;

const app = createApp({
    data(){
        return{
            datos: [],
            firstName: "",
            lastName: "",
            account: [],
            id: (new URLSearchParams(location.search)).get("id"),
            transactions: [],
        }
    },

    created(){
        this.loadData();
    },

    methods:{
        loadData(){
            axios.get('/api/clients/current/accounts/' + this.id)
            .then(response => {
                this.account = response.data;
                console.log(this.account);
                this.transactions = this.account.transactionsDTO;
                this.transactions.sort((a, b) => b.id - a.id);
                console.log(this.transactions);
            })
            .catch(error => console.log(error))
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