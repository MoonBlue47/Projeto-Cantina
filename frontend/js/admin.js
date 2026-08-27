// Estado do Admin
let adminProducts = [];

// Inicialização
document.addEventListener('DOMContentLoaded', () => {
  // Configuração da Sidebar
  const sidebarNav = document.getElementById('sidebarNav');
  if (sidebarNav) {
    sidebarNav.addEventListener('click', (e) => {
      const item = e.target.closest('.nav-item');
      if (!item) return;

      // Atualizar Menu Ativo
      document.querySelectorAll('.nav-item').forEach(nav => nav.classList.remove('active'));
      item.classList.add('active');

      // Trocar View
      const viewId = item.getAttribute('data-view');
      document.querySelectorAll('.view-section').forEach(view => {
        view.style.display = 'none';
      });
      
      const activeView = document.getElementById(`view-${viewId}`);
      if (activeView) activeView.style.display = 'block';

      // Se for a view de produtos, carregar dados reais do Spring Boot / MySQL
      if (viewId === 'produtos') {
        loadProdutos();
      }
    });
  }

  // Configurar Form de Produto
  const formProduto = document.getElementById('formProduto');
  if (formProduto) {
    formProduto.addEventListener('submit', salvarProduto);
  }
});

// Lógica de Produtos - Buscando do Banco de Dados via API
async function loadProdutos() {
  try {
    const response = await fetch('/produtos/api/listar');
    if (response.ok) {
      adminProducts = await response.json();
      renderTabelaProdutos();
    } else {
      console.error("Erro ao carregar produtos do servidor.");
    }
  } catch (error) {
    console.error("Erro de conexão ao carregar produtos:", error);
  }
}

function renderTabelaProdutos() {
  const tbody = document.getElementById('tabelaProdutos');
  if (!tbody) return;

  tbody.innerHTML = '';
  
  if (adminProducts.length === 0) {
    tbody.innerHTML = `<tr><td colspan="5" style="text-align: center; color: #777;">Nenhum produto cadastrado no banco.</td></tr>`;
    return;
  }

  adminProducts.forEach(p => {
    // Tratando a categoria caso venha como objeto ou nula
    let nomeCategoria = 'Geral';
    if (p.categoria) {
      nomeCategoria = typeof p.categoria === 'object' ? (p.categoria.nome || 'Categoria #' + p.categoria.id) : p.categoria;
    }

    const tr = document.createElement('tr');
    tr.innerHTML = `
      <td style="font-weight: 600;">#${p.id || ''}</td>
      <td>${p.nome || ''}</td>
      <td><span style="background: var(--light-blue, #e0f2fe); padding: 4px 12px; border-radius: 12px; font-size: 0.8rem; font-weight: bold;">${nomeCategoria}</span></td>
      <td style="color: var(--primary-red, #ef4444); font-weight: bold;">R$ ${Number(p.precoVendas || p.preco || 0).toFixed(2).replace('.', ',')}</td>
      <td>
        <button class="btn btn-secondary" style="padding: 6px 12px; font-size: 0.8rem;" onclick="window.showToast('Funcionalidade de edição em breve!', 'info')">Editar</button>
      </td>
    `;
    tbody.appendChild(tr);
  });
}

// Modais
window.abrirModalProduto = function() {
  const modal = document.getElementById('modalProduto');
  if (modal) modal.classList.add('active');
}

window.fecharModalProduto = function() {
  const modal = document.getElementById('modalProduto');
  if (modal) modal.classList.remove('active');
  
  const form = document.getElementById('formProduto');
  if (form) form.reset();
}

// Enviando dados para o Spring Boot salvar no MySQL (Com ajuste para @ManyToOne)
async function salvarProduto(e) {
  e.preventDefault();
  
  const nome = document.getElementById('prodNome').value;
  const categoriaId = document.getElementById('prodCat').value;
  const precoVendas = parseFloat(document.getElementById('prodPreco').value);

  // Monta o objeto com a categoria estruturada como objeto contendo o ID
  const novoProduto = {
    nome: nome,
    precoVendas: precoVendas,
    categoria: {
      id: Number(categoriaId)
    }
  };

  try {
    const response = await fetch('/produtos/api/salvar', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(novoProduto)
    });

    if (response.ok) {
      if (window.showToast) window.showToast('Produto salvo no banco com sucesso!', 'success');
      fecharModalProduto();
      loadProdutos(); // Recarrega a tabela direto do MySQL
    } else {
      const erroMsg = await response.text();
      if (window.showToast) window.showToast('Erro: ' + erroMsg, 'error');
    }
  } catch (err) {
    console.error(err);
    if (window.showToast) {
      window.showToast('Erro de conexão com o servidor.', 'error');
    } else {
      alert('Erro de conexão com o servidor.');
    }
  }
}