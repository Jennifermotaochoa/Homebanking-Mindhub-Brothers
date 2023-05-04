console.log("Hola");
const {createApp} = Vue;

const app = createApp({
    data(){
        return{
            accounts: [],
            amount: "",
            description: "",
            numberOriginAccount: "",
            numberDestinationAccount: "",
            cuentaOrigen: true,
            cuentaDestino: false
        }
    },

    created(){
        this.loadData();
    },

    methods:{
        loadData() {
            axios.get("/api/clients/current")
                .then(response => {
                    this.accounts = response.data.accountsDTO;
                    console.log(this.accounts)
                })
                .catch(error => console.log("error"))
        },

        newTransfer(){
            console.log(this.amount, this.description, this.numberOriginAccount, this.numberDestinationAccount)
            
            axios.post('/api/clients/current/transactions', 'amount=' + this.amount + '&description=' + this.description + '&numberOriginAccount=' + this.numberOriginAccount + '&numberDestinationAccount=' + this.numberDestinationAccount)
            .then(response =>  window.location.href="/web/accounts.html")
            .catch(error => console.log(error.response.data))
        },
        
        logout(){
            axios.post('/api/logout')
            .then(response => 
                window.location.href="/web/index.html")
            .catch(error => console.log(error))
        },

        originAccount(){
            this.cuentaOrigen = true
            this.cuentaDestino = false
        },

        destinationAccount(){
            this.cuentaDestino = true
            this.cuentaOrigen = false
        }
    }

})

app.mount("#app");