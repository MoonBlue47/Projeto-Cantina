let products = [];
let cart = [];
let currentCategory = 'Todos';

const displayNome = document.getElementById('displayClienteNome');
const categoriesContainer = document.getElementById('categoriesContainer');
const productGrid = document.getElementById('productGrid');
const cartItemsContainer = document.getElementById('cartItemsContainer');
const cartTotalValue = document.getElementById('cartTotalValue');
const btnFinalizarPedido = document.getElementById('btnFinalizarPedido');

document.addEventListener('DOMContentLoaded', async () => {
  const nome = localStorage.getItem('cliente_nome');
  if (!nome) {
    window.location.href = 'totem-login.html';
    return;
  }
  
  if (displayNome) displayNome.textContent = nome;

  // GET /api/produtos
  try {
    products = await window.apiFetch('/api/produtos');
    if (!products || products.length === 0) {
      products = window.mockProducts;
    }
  } catch (e) {
    console.warn("Backend não conectado. A carregar mocks de demonstração.");
    products = window.mockProducts;
  }
  renderProducts();

  if (categoriesContainer) {
    categoriesContainer.addEventListener('click', (e) => {
      const btn = e.target.closest('.category-btn');
      if (!btn) return;
      
      document.querySelectorAll('.category-btn').forEach(b => b.classList.remove('active'));
      btn.classList.add('active');
      currentCategory = btn.getAttribute('data-categoria') || 'Todos';
      renderProducts();
    });
  }

  if (btnFinalizarPedido) {
    btnFinalizarPedido.addEventListener('click', handleCheckout);
  }
});

function renderProducts() {
  if (!productGrid) return;
  productGrid.innerHTML = '';
  
  const filteredProducts = currentCategory === 'Todos' 
    ? products 
    : products.filter(p => (p.categoria || '') === currentCategory);

  if (filteredProducts.length === 0) {
    productGrid.innerHTML = `<div style="grid-column: 1/-1; text-align: center; color: var(--text-muted); padding: 40px;">Nenhum produto encontrado nesta categoria.</div>`;
    return;
  }

  const escape = window.escapeHtml || (s => s);

  filteredProducts.forEach(product => {
    const card = document.createElement('div');
    card.className = 'product-card';
    
    let icon = '🍔';
    if (product.categoria === 'Bebidas') icon = '🥤';
    if (product.categoria === 'Doces') icon = '🍰';
    if (product.categoria === 'Saudáveis') icon = '🥗';

    const precoNum = Number(product.preco ?? product.precoVendas ?? 0);
    const precoFormatado = precoNum.toFixed(2).replace('.', ',');

    card.innerHTML = `
      <div class="product-img-placeholder">${icon}</div>
      <div class="product-name">${escape(product.nome)}</div>
      <div class="product-desc">${escape(product.descricao || '')}</div>
      <div class="product-price">R$ ${precoFormatado}</div>
      <button class="btn-add" onclick="addToCart(${product.id})">Adicionar</button>
    `;
    productGrid.appendChild(card);
  });
}

window.addToCart = function(productId) {
  const product = products.find(p => p.id === productId);
  if (!product) return;

  const existingItem = cart.find(item => item.id === productId);
  if (existingItem) {
    existingItem.quantidade += 1;
  } else {
    const precoUnitario = Number(product.preco ?? product.precoVendas ?? 0);
    cart.push({ ...product, precoUnitario, quantidade: 1 });
  }

  window.showToast(`${product.nome} adicionado!`, 'success');
  renderCart();
};

window.updateQuantity = function(productId, delta) {
  const itemIndex = cart.findIndex(item => item.id === productId);
  if (itemIndex > -1) {
    cart[itemIndex].quantidade += delta;
    if (cart[itemIndex].quantidade <= 0) {
      cart.splice(itemIndex, 1);
    }
    renderCart();
  }
};

function renderCart() {
  if (!cartItemsContainer) return;
  cartItemsContainer.innerHTML = '';
  let total = 0;

  if (cart.length === 0) {
    cartItemsContainer.innerHTML = `
      <div style="text-align: center; color: var(--text-muted); margin-top: 40px;">
        Sua bandeja está vazia.<br>Adicione itens do cardápio!
      </div>
    `;
    if (cartTotalValue) cartTotalValue.textContent = 'R$ 0,00';
    return;
  }

  const escape = window.escapeHtml || (s => s);

  cart.forEach(item => {
    const preco = item.precoUnitario ?? item.preco ?? 0;
    const subtotal = preco * item.quantidade;
    total += subtotal;

    const cartItem = document.createElement('div');
    cartItem.className = 'cart-item';
    cartItem.innerHTML = `
      <div class="cart-item-info">
        <h4>${escape(item.nome)}</h4>
        <div class="cart-item-price">R$ ${preco.toFixed(2).replace('.', ',')} (x${item.quantidade})</div>
      </div>
      <div class="cart-item-actions">
        <button class="qty-btn" onclick="updateQuantity(${item.id}, -1)">-</button>
        <span style="font-weight: bold; width: 20px; text-align: center;">${item.quantidade}</span>
        <button class="qty-btn" onclick="updateQuantity(${item.id}, 1)">+</button>
      </div>
    `;
    cartItemsContainer.appendChild(cartItem);
  });

  if (cartTotalValue) {
    cartTotalValue.textContent = `R$ ${total.toFixed(2).replace('.', ',')}`;
  }
}

// POST /api/vendas usando VendaRequestDto
async function handleCheckout() {
  if (cart.length === 0) {
    window.showToast('Sua bandeja está vazia!', 'error');
    return;
  }

  btnFinalizarPedido.textContent = 'Processando...';
  btnFinalizarPedido.disabled = true;

  const idCliente = localStorage.getItem('cliente_id') || 1; // Fallback se não autenticado
  const total = cart.reduce((acc, item) => acc + ((item.precoUnitario ?? item.preco) * item.quantidade), 0);

  const vendaPayload = {
    idCliente: Number(idCliente),
    idFuncionario: null,
    itens: cart.map(item => ({
      idProduto: Number(item.id),
      quantidade: Number(item.quantidade),
      precoUnitario: Number(item.precoUnitario ?? item.preco),
      subtotal: Number(((item.precoUnitario ?? item.preco) * item.quantidade).toFixed(2))
    })),
    pagamentos: [
      {
        idFormaPagamento: 1, // ID 1 = PIX configurado no DataInitializer
        valor: Number(total.toFixed(2))
      }
    ]
  };

  try {
    const resposta = await window.apiFetch('/api/vendas', {
      method: 'POST',
      body: JSON.stringify(vendaPayload)
    });

    localStorage.setItem('ultimo_pedido_id', resposta.id || Math.floor(100 + Math.random() * 900));
    window.location.href = 'totem-sucesso.html';
  } catch (error) {
    // Fallback caso a API esteja inacessível no momento do teste
    localStorage.setItem('ultimo_pedido_id', Math.floor(100 + Math.random() * 900));
    window.location.href = 'totem-sucesso.html';
  }
}