console.log("Hola");
const {createApp} = Vue;

const app = createApp({
    data(){
        return{
            accounts: [],
            loans: [],
            loanId: "",
            amount: 0,
            payments: "",
            numberAccount: "",
            selectLoan:"",
            filterPayments: "",
            filterLoan: "",
            amountInterest: 0,
            paymentsInterest: 0,
        }
    },

    created(){
        this.accountsData(),
        this.loansData()
    },

    methods:{
        accountsData(){
            axios.get('/api/clients/current/accounts')
            .then(response => {
                this.accounts = response.data.filter(account => account.active);
                console.log(this.accounts)
            })
            .catch(error => console.log(error))
        },

        loansData(){
            axios.get('/api/loans')
            .then(response => {
                this.loans = response.data;
                
                console.log(this.loans)
            })
            .catch(error => console.log(error))
        },  

        newLoan(){
            Swal.fire({
            title: 'Are you sure?',
            text: "You won't be able to revert this!",
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Accept'
            }).then(result => {
                if (result.isConfirmed){
                    axios.post('/api/loans', {"loanId": this.loanId, "amount": this.amount, "payments": this.payments, "numberAccount": this.numberAccount})
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
    computed:{
        selectPayments(){
            this.filterLoan = this.loans.filter(loan => loan.name == this.selectLoan)
            console.log(this.filterLoan)
            
            this.filterPayments = this.filterLoan.map(loan => loan.payments)
            console.log(this.filterPayments)

            if(this.filterLoan.length){
                this.loanId = this.filterLoan[0].id
                console.log(this.loanId)
            }
        },

        interestLoan(){
            this.amountInterest = parseInt((this.amount * 0.2)) + parseInt(this.amount)
            console.log(this.amountInterest)
            this.paymentsInterest = parseFloat(this.amountInterest / parseInt(this.payments))
        },
    }
})


app.mount("#app");