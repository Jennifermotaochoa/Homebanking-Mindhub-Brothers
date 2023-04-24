console.log("Hola");
const {createApp} = Vue;

const app = createApp({
    data(){
        return{
            datos: [],
            cards: [],
            cardsGold: [],
            cardsTitanium: [],
            cardsSilver: [],
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

                this.cardsGold = this.cards.filter(card => card.color === "GOLD");
                console.log(this.cardsGold);

                this.cardsTitanium = this.cards.filter(card => card.color === "TITANIUM");
                console.log(this.cardsTitanium);

                this.cardsSilver = this.cards.filter(card => card.color === "SILVER");
                console.log(this.cardsSilver);


            })
            .catch(error => console.log(error))
        },
    }

})

app.mount("#app");