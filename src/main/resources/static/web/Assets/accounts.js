console.log("Hola");
const {createApp} = Vue;

const app = createApp({
    data(){
        return{
            datos: [],
            firstName: "",
            lastName: "",
            loans:[],
            accounts: [],
            accountsActive: [],
            accountType: "",
            selectAccount: "",
            selectPay: 0,
            quota: 0,
            total: 0,
            selectLoan: "",
            loanId: ""
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

                this.accounts = this.datos.accountsDTO.filter(account => account.active);
                console.log(this.accounts);

                this.loans = this.datos.loans.filter(loan => loan.active);
                console.log(this.loans);
                
            })
            .catch(error => console.log(error))
        },

        newAccount(){
            axios.post('/api/clients/current/accounts', 'accountType=' + this.accountType)
            .then(response => 
                window.location.href="/web/accounts.html")
                
            .catch(error => Swal.fire({
                title: 'Error',
                text: error.response.data,
                icon: 'error'
            }))
        },

        deleteAccount(id){
            Swal.fire({
                title: 'Are you sure?',
                text: "You won't be able to revert this!",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Delete'
              }).then(result => {
                    if (result.isConfirmed){
                        axios.put(`/api/clients/current/accounts/${id}`)
                        .then(response =>  window.location.href="/web/accounts.html")
                        .catch(error => Swal.fire({
                            title: 'Error',
                            text: error.response.data,
                            icon: 'error'
                        }))   
                    }
            })
        },

        payLoan(){
           Swal.fire({
                title: 'Are you sure?',
                text: "You won't be able to revert this!",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Pay'
              }).then(result => {
                    if (result.isConfirmed){
                        axios.post('/api/loans/pay', 'loanId=' + this.selectLoan+ '&amount=' + this.selectPay + '&account=' + this.selectAccount)
                        .then(response =>  window.location.href="/web/accounts.html")
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
    },
})

app.mount("#app");