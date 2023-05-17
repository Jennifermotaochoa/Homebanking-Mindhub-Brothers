console.log("Hola");
const {createApp} = Vue;

const app = createApp({
    data(){
        return{
            datos: [],
            cards: [],
            cardsCredit: [],
            cardsDebit: [],
            cardsActive: [],
            cardType: "",
            colorType: "",
            currentDate: ""
        }
    },

    created(){
        this.loadData();
    },

    methods:{
        loadData(){
            axios.get('/api/clients/current')
            .then(response => {
                this.datos = response.data;
                console.log(this.datos);

                this.cards = this.datos.cards
                console.log(this.cards);

                this.cardsDebit = this.cards.filter(card => card.type === "DEBIT" && card.active);
                console.log(this.cardsDebit);

                this.cardsCredit = this.cards.filter(card => card.type === "CREDIT" && card.active);
                console.log(this.cardsCredit);

                this.cardsActive = this.cards.filter(card => card.active);
                console.log(this.cardsActive);

                this.currentDate = new Date().toLocaleDateString().split(",")[0].split("/").reverse().join("-");
                console.log(this.currentDate);
            })
            .catch(error => console.log(error))
        },

        newCard(){
            axios.post('/api/clients/current/cards', 'cardType=' + this.cardType + '&colorType=' + this.colorType)
            .then(response => 
                window.location.href="/web/cards.html")

            .catch(error =>  Swal.fire({
                title: 'Error',
                text: error.response.data,
                icon: 'error'
            }))
        },
        
        deleteCard(id){
            Swal.fire({
                title: 'Are you sure?',
                text: "You won't be able to revert this!",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Delete card'
              }).then(result => {
                    if (result.isConfirmed){
                        axios.put(`/api/clients/current/cards/${id}`)
                        .then(response =>  window.location.href="/web/cards.html")
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