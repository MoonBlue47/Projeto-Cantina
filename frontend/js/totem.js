// Estado Global do Totem
let products = [];
let cart = [];
let currentCategory = 'Todos';

// Elementos DOM
const displayNome = document.getElementById('displayClienteNome');
const categoriesContainer = document.getElementById('categoriesContainer');
const productGrid = document.getElementById('productGrid');
const cartItemsContainer = document.getElementById('cartItemsContainer');
const cartTotalValue = document.getElementById('cartTotalValue');
const btnFinalizarPedido = document.getElementById('btnFinalizarPedido');

// Inicialização
document.addEventListener('DOMContentLoaded', async () => {
  const nome = localStorage.getItem('cliente_nome');
  if (!nome) {
    window.location.href = 'totem-login.html';
    return;
  }
  
  if (displayNome) displayNome.textContent = nome;

  // Carregar produtos (MOCK por enquanto, ou fetch se o backend existir)
  try {
    // let data = await window.apiFetch('/produtos');
    // products = data;
    products = window.mockProducts; 
    renderProducts();
  } catch (e) {
    products = window.mockProducts; // fallback
    renderProducts();
  }

  // Event Listeners das Categorias
  if (categoriesContainer) {
    categoriesContainer.addEventListener('click', (e) => {
      if (e.target.classList.contains('category-btn')) {
        document.querySelectorAll('.category-btn').forEach(btn => btn.classList.remove('active'));
        e.target.classList.add('active');
        currentCategory = e.target.getAttribute('data-categoria');
        renderProducts();
      }
    });
  }

  if (btnFinalizarPedido) {
    btnFinalizarPedido.addEventListener('click', handleCheckout);
  }
});

// Renderizar Produtos
function renderProducts() {
  if (!productGrid) return;
  
  productGrid.innerHTML = '';
  
  const filteredProducts = currentCategory === 'Todos' 
    ? products 
    : products.filter(p => p.categoria === currentCategory);

  if (filteredProducts.length === 0) {
    productGrid.innerHTML = `<div style="grid-column: 1/-1; text-align: center; color: var(--text-muted); padding: 40px;">Nenhum produto encontrado nesta categoria.</div>`;
    return;
  }

  filteredProducts.forEach(product => {
    const card = document.createElement('div');
    card.className = 'product-card';
    
    // Ícone por categoria (simulando imagem)
    let icon = '🍔';
    if (product.categoria === 'Bebidas') icon = '🥤';
    if (product.categoria === 'Doces') icon = '🍰';
    if (product.categoria === 'Saudáveis') icon = '🥗';

    card.innerHTML = `
      <div class="product-img-placeholder">${icon}</div>
      <div class="product-name">${product.nome}</div>
      <div class="product-desc">${product.descricao}</div>
      <div class="product-price">R$ ${product.preco.toFixed(2).replace('.', ',')}</div>
      <button class="btn-add" onclick="addToCart(${product.id})">Adicionar</button>
    `;
    productGrid.appendChild(card);
  });
}

// Lógica do Carrinho
window.addToCart = function(productId) {
  const product = products.find(p => p.id === productId);
  if (!product) return;

  const existingItem = cart.find(item => item.id === productId);
  if (existingItem) {
    existingItem.quantidade += 1;
  } else {
    cart.push({ ...product, quantidade: 1 });
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
    cartTotalValue.textContent = 'R$ 0,00';
    return;
  }

  cart.forEach(item => {
    const subtotal = item.preco * item.quantidade;
    total += subtotal;

    const cartItem = document.createElement('div');
    cartItem.className = 'cart-item';
    cartItem.innerHTML = `
      <div class="cart-item-info">
        <h4>${item.nome}</h4>
        <div class="cart-item-price">R$ ${item.preco.toFixed(2).replace('.', ',')} (x${item.quantidade})</div>
      </div>
      <div class="cart-item-actions">
        <button class="qty-btn" onclick="updateQuantity(${item.id}, -1)">-</button>
        <span style="font-weight: bold; width: 20px; text-align: center;">${item.quantidade}</span>
        <button class="qty-btn" onclick="updateQuantity(${item.id}, 1)">+</button>
      </div>
    `;
    cartItemsContainer.appendChild(cartItem);
  });

  cartTotalValue.textContent = `R$ ${total.toFixed(2).replace('.', ',')}`;
}

// Finalizar Pedido
async function handleCheckout() {
  if (cart.length === 0) {
    window.showToast('Sua bandeja está vazia!', 'error');
    return;
  }

  btnFinalizarPedido.textContent = 'Processando...';
  btnFinalizarPedido.disabled = true;

  try {
    // Simular delay de rede
    await new Promise(r => setTimeout(r, 1000));
    
    // const payload = { cliente: localStorage.getItem('cliente_matricula'), itens: cart };
    // await window.apiFetch('/vendas', { method: 'POST', body: JSON.stringify(payload) });
    
    window.location.href = 'totem-sucesso.html';
  } catch (error) {
    btnFinalizarPedido.textContent = 'Finalizar Pedido';
    btnFinalizarPedido.disabled = false;
  }
}
