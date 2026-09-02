// Estado Global do Totem
let products = [];
let cart = [];
let currentCategory = 'Todos';
let formasPagamento = [];

// Elementos DOM
const displayNome = document.getElementById('displayClienteNome');
const categoriesContainer = document.getElementById('categoriesContainer');
const productGrid = document.getElementById('productGrid');
const cartItemsContainer = document.getElementById('cartItemsContainer');
const cartTotalValue = document.getElementById('cartTotalValue');
const btnFinalizarPedido = document.getElementById('btnFinalizarPedido');
const inputBusca = document.getElementById('inputBuscaProduto');

// Helper de Imagem do Produto
function getImagemProduto(id) {
  try {
    const map = JSON.parse(localStorage.getItem('cantina_produto_imagens')) || {};
    return map[id] || null;
  } catch (_) {
    return null;
  }
}

// Função de Logout do Totem
window.logoutTotem = function () {
  localStorage.removeItem('cliente_nome');
  localStorage.removeItem('cliente_matricula');
  localStorage.removeItem('cliente_id');
  localStorage.removeItem('ultimo_pedido_id');
  window.location.href = 'totem-login.html';
};

// Inicialização
document.addEventListener('DOMContentLoaded', async () => {
  const nome = localStorage.getItem('cliente_nome');
  if (!nome) {
    window.location.href = 'totem-login.html';
    return;
  }
  if (displayNome) displayNome.textContent = nome;

  // Carregar produtos e categorias do banco
  await Promise.all([carregarProdutos(), carregarFormasPagamento()]);

  // Event Listeners das Categorias (delegação)
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

  // Listener de Busca Instantânea por Nome
  if (inputBusca) {
    inputBusca.addEventListener('input', () => {
      renderProducts();
    });
  }

  if (btnFinalizarPedido) {
    btnFinalizarPedido.addEventListener('click', window.abrirModalPagamento);
  }

  // Checar se veio com a URL ?aba=pedidos (ex: vindo da tela de sucesso)
  const urlParams = new URLSearchParams(window.location.search);
  if (urlParams.get('aba') === 'pedidos') {
    window.alternarAbaTotem('pedidos');
  }
});

// ─── Carregamento de Dados ───────────────────────────────────────────────────

async function carregarProdutos() {
  try {
    const data = await window.apiFetch('/produtos');
    products = data;

    // Montar botões de categoria dinamicamente
    const categorias = ['Todos', ...new Set(products.map(p => p.categoria))];
    if (categoriesContainer) {
      categoriesContainer.innerHTML = '';
      categorias.forEach(cat => {
        const btn = document.createElement('button');
        btn.className = 'category-btn' + (cat === 'Todos' ? ' active' : '');
        btn.setAttribute('data-categoria', cat);
        btn.textContent = cat;
        categoriesContainer.appendChild(btn);
      });
    }

    renderProducts();
  } catch (e) {
    if (productGrid) {
      productGrid.innerHTML = `<div style="grid-column:1/-1; text-align:center; color:#e53e3e; padding:40px;">
        Erro ao carregar cardápio. Verifique se o servidor está rodando.
      </div>`;
    }
  }
}

async function carregarFormasPagamento() {
  try {
    const data = await window.apiFetch('/formas-pagamento');
    formasPagamento = Array.isArray(data) ? data : [];
  } catch (_) {
    formasPagamento = [];
  }
}

// ─── Renderização de Produtos ────────────────────────────────────────────────

function renderProducts() {
  if (!productGrid) return;
  productGrid.innerHTML = '';

  const termoBusca = (inputBusca ? inputBusca.value : '').toLowerCase().trim();

  let filteredProducts = currentCategory === 'Todos'
    ? products
    : products.filter(p => p.categoria === currentCategory);

  if (termoBusca) {
    filteredProducts = filteredProducts.filter(p => (p.nome || '').toLowerCase().includes(termoBusca));
  }

  if (filteredProducts.length === 0) {
    productGrid.innerHTML = `<div style="grid-column:1/-1; text-align:center; color:var(--text-muted); padding:40px;">
      Nenhum produto encontrado.
    </div>`;
    return;
  }

  filteredProducts.forEach(product => {
    const card = document.createElement('div');
    const semEstoque = (product.estoque === undefined || product.estoque <= 0);
    card.className = 'product-card' + (semEstoque ? ' out-of-stock' : '');

    const imgData = getImagemProduto(product.id);
    let imgHtml = '';
    if (imgData) {
      imgHtml = `<img src="${imgData}" class="product-img-real" alt="${product.nome}">`;
    } else {
      imgHtml = `<div class="product-img-placeholder" style="font-size:0.9rem; color:var(--text-muted);">Sem Foto</div>`;
    }

    const badgeHtml = semEstoque
      ? `<span class="stock-badge out-of-stock-badge">Indisponível (Sem Estoque)</span>`
      : `<span class="stock-badge in-stock">${product.estoque} em estoque</span>`;

    const buttonHtml = semEstoque
      ? `<button class="btn-add" disabled style="cursor:not-allowed;">Indisponível</button>`
      : `<button class="btn-add" onclick="addToCart(${product.id})">Adicionar</button>`;

    card.innerHTML = `
      ${imgHtml}
      <div class="product-name">${product.nome}</div>
      <div class="product-desc">${product.descricao || product.categoria}</div>
      ${badgeHtml}
      <div class="product-price">R$ ${Number(product.preco).toFixed(2).replace('.', ',')}</div>
      ${buttonHtml}
    `;
    productGrid.appendChild(card);
  });
}

// ─── Navegação por Abas (Cardápio / Meus Pedidos) ──────────────────────────────

window.alternarAbaTotem = function (aba) {
  const secCardapio = document.getElementById('secCardapio');
  const secMeusPedidos = document.getElementById('secMeusPedidos');
  const tabCardapio = document.getElementById('tabCardapio');
  const tabMeusPedidos = document.getElementById('tabMeusPedidos');

  if (aba === 'cardapio') {
    if (secCardapio) secCardapio.style.display = 'flex';
    if (secMeusPedidos) secMeusPedidos.style.display = 'none';
    if (tabCardapio) tabCardapio.classList.add('active');
    if (tabMeusPedidos) tabMeusPedidos.classList.remove('active');
  } else {
    if (secCardapio) secCardapio.style.display = 'none';
    if (secMeusPedidos) secMeusPedidos.style.display = 'flex';
    if (tabCardapio) tabCardapio.classList.remove('active');
    if (tabMeusPedidos) tabMeusPedidos.classList.add('active');
    window.carregarMeusPedidos();
  }
};

// ─── Área Meus Pedidos ───────────────────────────────────────────────────────

window.carregarMeusPedidos = async function () {
  const container = document.getElementById('containerMeusPedidos');
  if (!container) return;

  const clienteIdStr = localStorage.getItem('cliente_id');
  const clienteId = clienteIdStr ? Number(clienteIdStr) : null;

  container.innerHTML = `<div style="text-align:center; color:var(--text-muted); padding:40px;">Carregando seus pedidos...</div>`;

  try {
    const vendas = await window.apiFetch('/vendas');
    const meusPedidos = clienteId
      ? vendas.filter(v => Number(v.idCliente) === clienteId)
      : vendas;

    if (!meusPedidos || meusPedidos.length === 0) {
      container.innerHTML = `
        <div class="card" style="text-align:center; padding:40px; color:var(--text-muted);">
          <h3 style="margin-bottom:8px; color:var(--text-dark);">Você ainda não possui pedidos registrados.</h3>
          <p>Faça seu primeiro pedido no cardápio!</p>
        </div>`;
      return;
    }

    meusPedidos.sort((a, b) => b.id - a.id);

    container.innerHTML = '';
    meusPedidos.forEach(p => {
      const card = document.createElement('div');
      card.className = 'pedido-card';

      const dataStr = p.dataVenda ? new Date(p.dataVenda).toLocaleString('pt-BR') : 'Data não informada';
      const statusStr = (p.status || 'PENDENTE').toUpperCase();
      let statusClass = 'pendente';
      if (statusStr.includes('CONCLU') || statusStr.includes('PAGO')) statusClass = 'concluido';
      else if (statusStr.includes('CANCEL')) statusClass = 'cancelado';

      let itensHtml = '';
      if (p.itens && p.itens.length > 0) {
        itensHtml = p.itens.map(item => {
          const prodObj = products.find(prod => prod.id === item.idProduto);
          const nomeProd = prodObj ? prodObj.nome : `Item #${item.idProduto}`;
          return `
            <div class="pedido-item-row">
              <span>${item.quantidade}x ${nomeProd}</span>
              <span>R$ ${Number(item.subtotal || (item.precoUnitario * item.quantidade) || 0).toFixed(2).replace('.', ',')}</span>
            </div>
          `;
        }).join('');
      } else {
        itensHtml = `<div style="color:var(--text-muted); font-style:italic;">Itens do pedido</div>`;
      }

      card.innerHTML = `
        <div class="pedido-header">
          <div>
            <div class="pedido-id">Pedido #${p.id}</div>
            <div class="pedido-data">Data: ${dataStr}</div>
          </div>
          <span class="status-badge ${statusClass}">${statusStr}</span>
        </div>
        <div class="pedido-itens-list">
          ${itensHtml}
        </div>
        <div class="pedido-total-row">
          <span>Total</span>
          <span class="text-red">R$ ${Number(p.valorTotal).toFixed(2).replace('.', ',')}</span>
        </div>
      `;
      container.appendChild(card);
    });
  } catch (err) {
    container.innerHTML = `
      <div style="text-align:center; color:var(--primary-red); padding:40px;">
        Erro ao carregar seus pedidos. Tente novamente em instantes.
      </div>`;
  }
};

// ─── Carrinho ────────────────────────────────────────────────────────────────

window.addToCart = function (productId) {
  const product = products.find(p => p.id === productId);
  if (!product) return;

  if (product.estoque === undefined || product.estoque <= 0) {
    window.showToast('Produto indisponível no momento!', 'error');
    return;
  }

  const existingItem = cart.find(item => item.id === productId);
  if (existingItem) {
    if (existingItem.quantidade >= product.estoque) {
      window.showToast(`Limite em estoque atingido (${product.estoque} un)!`, 'error');
      return;
    }
    existingItem.quantidade += 1;
  } else {
    cart.push({ ...product, quantidade: 1 });
  }

  window.showToast(`${product.nome} adicionado!`, 'success');
  renderCart();
};

window.updateQuantity = function (productId, delta) {
  const itemIndex = cart.findIndex(item => item.id === productId);
  if (itemIndex > -1) {
    cart[itemIndex].quantidade += delta;
    if (cart[itemIndex].quantidade <= 0) {
      cart.splice(itemIndex, 1);
    }
    renderCart();
  }
};

window.limparCarrinho = function () {
  if (cart.length === 0) return;
  cart = [];
  renderCart();
  window.showToast('Bandeja limpa com sucesso!', 'info');
};

function renderCart() {
  if (!cartItemsContainer) return;
  cartItemsContainer.innerHTML = '';
  let total = 0;

  if (cart.length === 0) {
    cartItemsContainer.innerHTML = `
      <div style="text-align:center; color:var(--text-muted); margin-top:40px;">
        Sua bandeja está vazia.<br>Adicione itens do cardápio!
      </div>`;
    if (cartTotalValue) cartTotalValue.textContent = 'R$ 0,00';
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
        <div class="cart-item-price">R$ ${Number(item.preco).toFixed(2).replace('.', ',')} (x${item.quantidade})</div>
      </div>
      <div class="cart-item-actions">
        <button class="qty-btn" onclick="updateQuantity(${item.id}, -1)">-</button>
        <span style="font-weight:bold; width:20px; text-align:center;">${item.quantidade}</span>
        <button class="qty-btn" onclick="updateQuantity(${item.id}, 1)">+</button>
      </div>
    `;
    cartItemsContainer.appendChild(cartItem);
  });

  if (cartTotalValue) cartTotalValue.textContent = `R$ ${total.toFixed(2).replace('.', ',')}`;
}

// ─── Modal de Pagamento ──────────────────────────────────────────────────────

window.abrirModalPagamento = function () {
  if (cart.length === 0) {
    window.showToast('Sua bandeja está vazia! Adicione produtos.', 'error');
    return;
  }

  const modal = document.getElementById('modalPagamento');
  const lista = document.getElementById('listaPagamentos');
  if (!modal || !lista) return;

  lista.innerHTML = '';

  if (formasPagamento.length === 0) {
    lista.innerHTML = `
      <p style="color:var(--text-muted); text-align:center; margin-bottom:12px;">
        Nenhuma forma de pagamento cadastrada.<br>O pedido será registrado como pendente.
      </p>
      <button class="btn btn-primary" style="width:100%;" onclick="window.confirmarPedido(null)">
        Confirmar Pedido
      </button>
    `;
  } else {
    formasPagamento.forEach(fp => {
      const btn = document.createElement('button');
      btn.className = 'btn btn-secondary';
      btn.style.cssText = 'padding:14px; font-size:1rem; text-align:left; width:100%;';
      btn.textContent = `${fp.tipo || fp.descricao || 'Pagamento #' + fp.id}`;
      btn.onclick = () => window.confirmarPedido(fp.id);
      lista.appendChild(btn);
    });
  }

  modal.style.display = 'flex';
  modal.classList.add('active');
};

window.fecharModalPagamento = function () {
  const modal = document.getElementById('modalPagamento');
  if (modal) {
    modal.classList.remove('active');
    setTimeout(() => { modal.style.display = 'none'; }, 200);
  }
};

// ─── Finalizar Pedido ────────────────────────────────────────────────────────

window.confirmarPedido = async function (idFormaPagamento) {
  window.fecharModalPagamento();

  const btn = document.getElementById('btnFinalizarPedido');
  if (btn) {
    btn.textContent = 'Processando...';
    btn.disabled = true;
  }

  const clienteId = localStorage.getItem('cliente_id')
    ? Number(localStorage.getItem('cliente_id'))
    : null;

  const total = cart.reduce((acc, item) => acc + item.preco * item.quantidade, 0);

  const payload = {
    idCliente: clienteId,
    idFuncionario: null,
    itens: cart.map(item => ({
      idProduto: item.id,
      quantidade: item.quantidade,
      precoUnitario: item.preco
    })),
    pagamentos: idFormaPagamento
      ? [{ idFormaPagamento, valor: total }]
      : []
  };

  try {
    const venda = await window.apiFetch('/vendas', {
      method: 'POST',
      body: JSON.stringify(payload)
    });

    localStorage.setItem('ultimo_pedido_id', venda.id);
    window.location.href = 'totem-sucesso.html';
  } catch (error) {
    if (btn) {
      btn.textContent = 'Finalizar Pedido';
      btn.disabled = false;
    }
    window.showToast('Erro ao finalizar pedido. Tente novamente.', 'error');
  }
};
