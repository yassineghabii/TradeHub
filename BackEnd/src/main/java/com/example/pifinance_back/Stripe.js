/*<form id="payment-form">
    <div id="card-element"></div>
    <div id="card-errors" role="alert"></div>
    <button id="submit">Payer</button>
</form>
<script>
    var stripe = Stripe('sk_test_51O5H2fKb1DjWinQwsDONxA9xfEQEQSjxr3AaCUVbnwsfaL8eCLhlyyzMiwSwvN3gXtjDI8D9bUO9wSBPpGT6AW2u00OtwW6gcG'); // Remplacez par votre clé publique
    var elements = stripe.elements();

    var card = elements.create('card');
    card.mount('#card-element');

    // Gestion des erreurs en temps réel
    card.addEventListener('change', function(event) {
    var displayError = document.getElementById('card-errors');
    if (event.error) {
    displayError.textContent = event.error.message;
} else {
    displayError.textContent = '';
}
});

    // Gestion de la soumission du formulaire
    var form = document.getElementById('payment-form');
    form.addEventListener('submit', function(event) {
    event.preventDefault();

    stripe.createToken(card).then(function(result) {
    if (result.error) {
    var errorElement = document.getElementById('card-errors');
    errorElement.textContent = result.error.message;
} else {
    stripeTokenHandler(result.token);
}
});
});

    function stripeTokenHandler(token) {
    // Insérez le token dans le formulaire afin qu'il soit soumis au serveur
    var form = document.getElementById('payment-form');
    var hiddenInput = document.createElement('input');
    hiddenInput.setAttribute('type', 'hidden');
    hiddenInput.setAttribute('name', 'stripeToken');
    hiddenInput.setAttribute('value', token.id);
    form.appendChild(hiddenInput);

    // Soumettez le formulaire
    form.submit();
}
</script> */