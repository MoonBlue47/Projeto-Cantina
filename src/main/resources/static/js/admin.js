let adminProducts = [];

document.addEventListener('DOMContentLoaded', () => {
  // Verificar sessão do admin
  const adminLogado = localStorage.getItem('admin_login');
  if (!adminLogado && !window.location.pathname.endsWith('admin-login.html')) {
    window.location.href = 'admin-login.html';
    return;
  }

  const sidebarNav = document.getElementById('sidebarNav');
  if (sidebarNav) {
    sidebarNav.addEventListener('click', (e) => {
      const item = e.target.closest('.nav-item');
      if (!item) return;

      document.querySelectorAll('.nav-item').forEach(nav => nav.classList.remove('active'));
      item.classList.add('active');

      const viewId = item.getAttribute('data-view');
      document.querySelectorAll('.view-section').forEach(view => {
        view.style.display = 'none';
      });
      
      const activeView = document.getElementById(`view-${viewId}`);
      if (activeView) activeView.style.display = 'block';

      if (viewId === 'produtos') {
        loadProdutos();
      }
    });
  }

  const formProduto = document.getElementById('formProduto');
  if (formProduto) {
    formProduto.addEventListener('submit', salvarProduto);
  }
});

// GET /api/produtos
async function loadProdutos() {
  try {
    adminProducts = await window.apiFetch('/api/produtos');
    renderTabelaProdutos();
  } catch (error) {
    console.warn("Utilizando dados mock devido a erro de conexão:", error);
    adminProducts = window.mockProducts || [];
    renderTabelaProdutos();
  }
}

function renderTabelaProdutos() {
  const tbody = document.getElementById('tabelaProdutos');
  if (!tbody) return;

  tbody.innerHTML = '';
  
  if (!adminProducts || adminProducts.length === 0) {
    tbody.innerHTML = `<tr><td colspan="5" style="text-align: center; color: #777;">Nenhum produto cadastrado no banco.</td></tr>`;
    return;
  }

  const escape = window.escapeHtml || (s => s);

  adminProducts.forEach(p => {
    const precoNum = Number(p.preco ?? p.precoVendas ?? 0);
    const precoFormatado = precoNum.toFixed(2).replace('.', ',');

    const tr = document.createElement('tr');
    tr.innerHTML = `
      <td style="font-weight: 600;">#${escape(p.id)}</td>
      <td>${escape(p.nome)}</td>
      <td><span style="background: var(--light-blue, #e0f2fe); padding: 4px 12px; border-radius: 12px; font-size: 0.8rem; font-weight: bold;">${escape(p.categoria || 'Geral')}</span></td>
      <td style="color: var(--primary-red, #ef4444); font-weight: bold;">R$ ${precoFormatado}</td>
      <td>
        <button class="btn btn-secondary" style="padding: 6px 12px; font-size: 0.8rem;" onclick="window.showToast('Funcionalidade de edição em breve!', 'info')">Editar</button>
      </td>
    `;
    tbody.appendChild(tr);
  });
}

window.abrirModalProduto = function() {
  const modal = document.getElementById('modalProduto');
  if (modal) modal.classList.add('active');
};

window.fecharModalProduto = function() {
  const modal = document.getElementById('modalProduto');
  if (modal) modal.classList.remove('active');
  
  const form = document.getElementById('formProduto');
  if (form) form.reset();
};

// POST /api/produtos com payload { nome, preco, idCategoria }
async function salvarProduto(e) {
  e.preventDefault();
  
  const nome = document.getElementById('prodNome')?.value.trim() || '';
  const idCategoria = Number(document.getElementById('prodCat')?.value || '1');
  const preco = parseFloat(document.getElementById('prodPreco')?.value || '0');

  const novoProduto = {
    nome: nome,
    preco: preco,
    idCategoria: idCategoria
  };

  try {
    await window.apiFetch('/api/produtos', {
      method: 'POST',
      body: JSON.stringify(novoProduto)
    });

    window.showToast('Produto cadastrado com sucesso no MySQL!', 'success');
    fecharModalProduto();
    loadProdutos();
  } catch (err) {
    // Erro tratado no apiFetch
  }
}