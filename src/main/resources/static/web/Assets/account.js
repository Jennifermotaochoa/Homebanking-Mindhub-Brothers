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
            accountNumber: "",
            dateStart: "",
            dateEnd: "",
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

        downloadPDF(){
            if(this.dateStart.length == 0 || this.dateEnd.length == 0){
                axios.get('/api/pdf/transactions', `accountNumber=${this.account.number}&start=all&end=all`)
                axios({
                    method: 'GET',
                    url: '/api/pdf/transactions',
                    responseType: 'blob', // Especificamos que la respuesta es un archivo blob
                    params: {
                        accountNumber: `${this.accountNumber}`,
                        start: 'all',
                        end: 'all'
                        }
                    })
                        .then(response => {
                        // Crear una URL del blob de la respuesta
                        const url = window.URL.createObjectURL(new Blob([response.data]));
                    
                        // Crear un enlace para descargar el archivo
                        const link = document.createElement('a');
                        link.href = url;
                        link.setAttribute('download', 'transaction-history.pdf');
                        document.body.appendChild(link);
                    
                        // Hacer clic en el enlace para iniciar la descarga
                        link.click();
                    
                        // Liberar la URL del blob
                        window.URL.revokeObjectURL(url);
                        })
                        .catch(error => {
                        console.error(error);
                });
            } else {
                window.location.replace(`/api/pdf/transactions?accountNumber=${this.accountNumber}&start=${this.dateStart}&end=${this.dateEnd}`)
            }
        },      
    }

})

app.mount("#app");