console.log("Hola");
const {createApp} = Vue;

const app = createApp({
    data(){
        return{
            datos: [],
            firstName: "",
            lastName: "",
            email: "",
            loans: [],
            name: "",
            amount: "",
            payments: "",
            interest: ""
        }
    },

    created(){
        this.loadData();
        this.loadLoans();
    },

    methods:{
        loadData(){
            axios.get('/api/clients')
            .then(response => {
                this.datos = response.data;
                console.log(this.datos);
            })
            .catch(error => console.log(error))
        },
        
        loadLoans(){
            axios.get('/api/loans')
            .then(response => {
                this.loans = response.data;
                console.log(this.loans);
            })
            .catch(error => console.log(error.response.data))
        },

        addClient(){
            axios.post('/api/clients',{
                firstName: this.firstName,
                lastName: this.lastName,
                email: this.email
            })
            .then(function(response){
                this.loadData();
            })
            .catch(error => console.log(error))
        },

        addLoan(){
            Swal.fire({
                title: 'Are you sure?',
                text: "You won't be able to revert this!",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Add new loan'
                }).then(result => {
                    if (result.isConfirmed){
                        const convertedPayments = this.payments.split(',').map(number => parseInt(number));
                        console.log(convertedPayments)
                        axios.post('/api/loans/admin', {
                            name: this.name,
                            amount: this.amount,
                            payments: convertedPayments,
                            interest: this.interest
                        })
                        .then(response => window.location.href="manager.html")
                        .catch(error => Swal.fire({
                            title: 'Error',
                            text: error.response.data,
                            icon: 'error'
                        }))   
                    }
                })
            
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