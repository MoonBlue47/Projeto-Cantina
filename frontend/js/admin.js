// ─── Estado Global do Admin ───────────────────────────────────────────────────
const adminNome = localStorage.getItem('admin_nome') || 'Admin';

// ─── Inicialização ────────────────────────────────────────────────────────────
document.addEventListener('DOMContentLoaded', () => {
  // Verificar autenticação
  if (!localStorage.getItem('admin_autenticado')) {
    window.location.href = 'admin-login.html';
    return;
  }

  // Exibir nome do usuário
  const nomeEl = document.getElementById('adminNomeExibicao');
  if (nomeEl) nomeEl.textContent = adminNome;

  // Navegação da Sidebar
  const sidebarNav = document.getElementById('sidebarNav');
  if (sidebarNav) {
    sidebarNav.addEventListener('click', (e) => {
      const item = e.target.closest('.nav-item');
      if (!item) return;
      document.querySelectorAll('.nav-item').forEach(n => n.classList.remove('active'));
      item.classList.add('active');
      const viewId = item.getAttribute('data-view');
      document.querySelectorAll('.view-section').forEach(v => v.style.display = 'none');
      const activeView = document.getElementById(`view-${viewId}`);
      if (activeView) activeView.style.display = 'block';
      carregarView(viewId);
    });
  }

  // Carregar view inicial
  carregarView('pedidos');
});

function carregarView(viewId) {
  const loaders = {
    pedidos:     loadPedidos,
    produtos:    loadProdutos,
    categorias:  loadCategorias,
    clientes:    loadClientes,
    tipoCliente: loadTiposCliente,
    funcionarios: loadFuncionarios,
    fornecedores: loadFornecedores,
    pagamentos:  loadFormasPagamento,
    estoque:     loadEstoque,
    usuarios:    loadUsuarios,
  };
  if (loaders[viewId]) loaders[viewId]();
}

// ─── Utilitários ──────────────────────────────────────────────────────────────
function criarTabela(tbodyId, colunas, dados, renderLinha) {
  const tbody = document.getElementById(tbodyId);
  if (!tbody) return;
  tbody.innerHTML = '';
  if (!dados || dados.length === 0) {
    tbody.innerHTML = `<tr><td colspan="${colunas}" style="text-align:center; color:var(--text-muted); padding:20px;">Nenhum registro encontrado.</td></tr>`;
    return;
  }
  dados.forEach(item => {
    const tr = document.createElement('tr');
    tr.innerHTML = renderLinha(item);
    tbody.appendChild(tr);
  });
}

function abrirModal(id) { document.getElementById(id).classList.add('active'); }
function fecharModal(id) {
  document.getElementById(id).classList.remove('active');
  const form = document.querySelector(`#${id} form`);
  if (form) form.reset();
}
window.fecharModal = fecharModal;

// ─── PEDIDOS ──────────────────────────────────────────────────────────────────
async function loadPedidos() {
  try {
    const vendas = await window.apiFetch('/vendas');
    criarTabela('tabelaPedidos', 6, vendas, v => `
      <td style="font-weight:600;">#${v.id}</td>
      <td>${v.dataVenda ? new Date(v.dataVenda).toLocaleString('pt-BR') : '-'}</td>
      <td>${v.idCliente ? 'Cliente #' + v.idCliente : 'Totem'}</td>
      <td style="color:var(--primary-red); font-weight:bold;">R$ ${Number(v.valorTotal).toFixed(2).replace('.',',')}</td>
      <td><span class="status-badge status-${(v.status||'').replace(' ','-')}">${v.status || '-'}</span></td>
      <td>
        <button class="btn btn-secondary" style="padding:4px 10px; font-size:0.8rem;" 
          onclick="atualizarStatusVenda(${v.id}, 'CONCLUIDO')">✅ Concluir</button>
        <button class="btn" style="padding:4px 10px; font-size:0.8rem; background:#fed7d7; color:#c53030;" 
          onclick="atualizarStatusVenda(${v.id}, 'CANCELADO')">✖ Cancelar</button>
      </td>
    `);
  } catch(err) { window.showToast(err.message || 'Erro ao comunicar com o servidor.', 'error'); }
}

window.atualizarStatusVenda = async function(id, status) {
  try {
    await window.apiFetch(`/vendas/${id}/status?status=${status}`, { method: 'PUT' });
    window.showToast('Status atualizado!', 'success');
    loadPedidos();
  } catch(err) { window.showToast(err.message || 'Erro ao comunicar com o servidor.', 'error'); }
};

// ─── PRODUTOS ─────────────────────────────────────────────────────────────────
let listaProdutosMemoria = [];

async function loadProdutos() {
  try {
    listaProdutosMemoria = await window.apiFetch('/produtos');
    criarTabela('tabelaProdutos', 5, listaProdutosMemoria, p => `
      <td style="font-weight:600;">#${p.id}</td>
      <td>${p.nome}</td>
      <td><span style="background:var(--light-blue); padding:4px 12px; border-radius:12px; font-size:0.8rem; font-weight:bold;">${p.categoria}</span></td>
      <td style="color:var(--primary-red); font-weight:bold;">R$ ${Number(p.preco).toFixed(2).replace('.',',')}</td>
      <td>
        <button class="btn btn-secondary" style="padding:4px 10px; font-size:0.8rem; margin-right:6px;"
          onclick="editarProduto(${p.id})">✏️ Editar</button>
        <button class="btn btn-secondary" style="padding:4px 10px; font-size:0.8rem;"
          onclick="excluirProduto(${p.id})">🗑 Excluir</button>
      </td>
    `);
  } catch(err) { window.showToast(err.message || 'Erro ao comunicar com o servidor.', 'error'); }
}

window.abrirModalProduto = async function() {
  document.getElementById('prodId').value = '';
  document.getElementById('modalProdutoTitle').textContent = 'Cadastrar Produto';
  const form = document.querySelector('#modalProduto form');
  if (form) form.reset();

  try {
    const cats = await window.apiFetch('/categorias');
    const sel = document.getElementById('prodCat');
    sel.innerHTML = '<option value="">Selecione...</option>';
    cats.forEach(c => { sel.innerHTML += `<option value="${c.id}">${c.nome}</option>`; });
  } catch(err) { window.showToast(err.message || 'Erro ao comunicar com o servidor.', 'error'); }

  abrirModal('modalProduto');
};

window.editarProduto = async function(id) {
  await window.abrirModalProduto();
  const produto = listaProdutosMemoria.find(p => p.id === id);
  if (produto) {
    document.getElementById('prodId').value = produto.id;
    document.getElementById('prodNome').value = produto.nome;
    document.getElementById('prodPreco').value = produto.preco;
    if (produto.idCategoria) {
      document.getElementById('prodCat').value = produto.idCategoria;
    }
    document.getElementById('modalProdutoTitle').textContent = 'Editar Produto';
  }
};

document.addEventListener('DOMContentLoaded', () => {
  const formProduto = document.getElementById('formProduto');
  if (formProduto) {
    formProduto.addEventListener('submit', async (e) => {
      e.preventDefault();
      const id = document.getElementById('prodId').value;
      const nome = document.getElementById('prodNome').value;
      const idCategoria = document.getElementById('prodCat').value;
      const preco = parseFloat(document.getElementById('prodPreco').value);

      const endpoint = id ? `/produtos/${id}` : '/produtos';
      const method = id ? 'PUT' : 'POST';

      try {
        await window.apiFetch(endpoint, {
          method,
          body: JSON.stringify({ nome, idCategoria: Number(idCategoria), preco })
        });
        window.showToast(id ? 'Produto atualizado!' : 'Produto cadastrado!', 'success');
        fecharModal('modalProduto');
        loadProdutos();
      } catch(err) { window.showToast(err.message || 'Erro ao comunicar com o servidor.', 'error'); }
    });
  }
});

window.excluirProduto = async function(id) {
  if (!confirm('Excluir este produto?')) return;
  try {
    await window.apiFetch(`/produtos/${id}`, { method: 'DELETE' });
    window.showToast('Produto excluído!', 'success');
    loadProdutos();
  } catch(err) { window.showToast(err.message || 'Erro ao comunicar com o servidor.', 'error'); }
};

// ─── CATEGORIAS ───────────────────────────────────────────────────────────────
let listaCategoriasMemoria = [];

async function loadCategorias() {
  try {
    listaCategoriasMemoria = await window.apiFetch('/categorias');
    criarTabela('tabelaCategorias', 4, listaCategoriasMemoria, c => `
      <td style="font-weight:600;">#${c.id}</td>
      <td>${c.nome}</td>
      <td>${c.descricao || '-'}</td>
      <td>
        <button class="btn btn-secondary" style="padding:4px 10px; font-size:0.8rem; margin-right:6px;"
          onclick="editarCategoria(${c.id})">✏️ Editar</button>
        <button class="btn btn-secondary" style="padding:4px 10px; font-size:0.8rem;"
          onclick="excluirCategoria(${c.id})">🗑 Excluir</button>
      </td>
    `);
  } catch(err) { window.showToast(err.message || 'Erro ao comunicar com o servidor.', 'error'); }
}

window.abrirModalCategoria = function() {
  document.getElementById('catId').value = '';
  document.getElementById('modalCategoriaTitle').textContent = 'Nova Categoria';
  const form = document.querySelector('#modalCategoria form');
  if (form) form.reset();
  abrirModal('modalCategoria');
};

window.editarCategoria = function(id) {
  const cat = listaCategoriasMemoria.find(c => c.id === id);
  if (cat) {
    document.getElementById('catId').value = cat.id;
    document.getElementById('catNome').value = cat.nome;
    document.getElementById('catDesc').value = cat.descricao || '';
    document.getElementById('modalCategoriaTitle').textContent = 'Editar Categoria';
    abrirModal('modalCategoria');
  }
};

document.addEventListener('DOMContentLoaded', () => {
  const formCategoria = document.getElementById('formCategoria');
  if (formCategoria) {
    formCategoria.addEventListener('submit', async (e) => {
      e.preventDefault();
      const id = document.getElementById('catId').value;
      const nome = document.getElementById('catNome').value;
      const descricao = document.getElementById('catDesc').value;

      const endpoint = id ? `/categorias/${id}` : '/categorias';
      const method = id ? 'PUT' : 'POST';

      try {
        await window.apiFetch(endpoint, {
          method,
          body: JSON.stringify({ nome, descricao })
        });
        window.showToast(id ? 'Categoria atualizada!' : 'Categoria cadastrada!', 'success');
        fecharModal('modalCategoria');
        loadCategorias();
      } catch(err) { window.showToast(err.message || 'Erro ao comunicar com o servidor.', 'error'); }
    });
  }
});

window.excluirCategoria = async function(id) {
  if (!confirm('Excluir esta categoria?')) return;
  try {
    await window.apiFetch(`/categorias/${id}`, { method: 'DELETE' });
    window.showToast('Categoria excluída!', 'success');
    loadCategorias();
  } catch(err) { window.showToast(err.message || 'Erro ao comunicar com o servidor.', 'error'); }
};

// ─── CLIENTES ─────────────────────────────────────────────────────────────────
async function loadClientes() {
  try {
    const clientes = await window.apiFetch('/clientes');
    criarTabela('tabelaClientes', 4, clientes, c => `
      <td style="font-weight:600;">#${c.id}</td>
      <td>${c.nome}</td>
      <td>${c.matricula}</td>
      <td>
        <button class="btn btn-secondary" style="padding:4px 10px; font-size:0.8rem;"
          onclick="excluirCliente(${c.id})">🗑</button>
      </td>
    `);
  } catch(err) { window.showToast(err.message || 'Erro ao comunicar com o servidor.', 'error'); }
}

window.abrirModalCliente = async function() {
  try {
    const tipos = await window.apiFetch('/tipos-cliente');
    const sel = document.getElementById('clienteTipo');
    sel.innerHTML = '<option value="">Selecione...</option>';
    tipos.forEach(t => { sel.innerHTML += `<option value="${t.id}">${t.nome}</option>`; });
  } catch(err) { window.showToast(err.message || 'Erro ao comunicar com o servidor.', 'error'); }
  abrirModal('modalCliente');
};

document.addEventListener('DOMContentLoaded', () => {
  const formCliente = document.getElementById('formCliente');
  if (formCliente) {
    formCliente.addEventListener('submit', async (e) => {
      e.preventDefault();
      const nome = document.getElementById('clienteNome').value;
      const matricula = document.getElementById('clienteMatricula').value;
      const idTipoCliente = Number(document.getElementById('clienteTipo').value);
      try {
        await window.apiFetch('/clientes', {
          method: 'POST',
          body: JSON.stringify({ nome, matricula, idTipoCliente })
        });
        window.showToast('Cliente cadastrado!', 'success');
        fecharModal('modalCliente');
        loadClientes();
      } catch(err) { window.showToast(err.message || 'Erro ao comunicar com o servidor.', 'error'); }
    });
  }
});

window.excluirCliente = async function(id) {
  if (!confirm('Excluir este cliente?')) return;
  try {
    await window.apiFetch(`/clientes/${id}`, { method: 'DELETE' });
    window.showToast('Cliente excluído!', 'success');
    loadClientes();
  } catch(err) { window.showToast(err.message || 'Erro ao comunicar com o servidor.', 'error'); }
};

// ─── TIPOS DE CLIENTE ─────────────────────────────────────────────────────────
async function loadTiposCliente() {
  try {
    const tipos = await window.apiFetch('/tipos-cliente');
    criarTabela('tabelaTiposCliente', 3, tipos, t => `
      <td style="font-weight:600;">#${t.id}</td>
      <td>${t.nome}</td>
      <td>
        <button class="btn btn-secondary" style="padding:4px 10px; font-size:0.8rem;"
          onclick="excluirTipoCliente(${t.id})">🗑</button>
      </td>
    `);
  } catch(err) { window.showToast(err.message || 'Erro ao comunicar com o servidor.', 'error'); }
}

document.addEventListener('DOMContentLoaded', () => {
  const formTipoCliente = document.getElementById('formTipoCliente');
  if (formTipoCliente) {
    formTipoCliente.addEventListener('submit', async (e) => {
      e.preventDefault();
      const nome = document.getElementById('tipoClienteNome').value;
      try {
        await window.apiFetch('/tipos-cliente', {
          method: 'POST',
          body: JSON.stringify({ nome })
        });
        window.showToast('Tipo de cliente cadastrado!', 'success');
        fecharModal('modalTipoCliente');
        loadTiposCliente();
      } catch(err) { window.showToast(err.message || 'Erro ao comunicar com o servidor.', 'error'); }
    });
  }
});

window.excluirTipoCliente = async function(id) {
  if (!confirm('Excluir este tipo de cliente?')) return;
  try {
    await window.apiFetch(`/tipos-cliente/${id}`, { method: 'DELETE' });
    window.showToast('Tipo excluído!', 'success');
    loadTiposCliente();
  } catch(err) { window.showToast(err.message || 'Erro ao comunicar com o servidor.', 'error'); }
};

// ─── FUNCIONÁRIOS ─────────────────────────────────────────────────────────────
async function loadFuncionarios() {
  try {
    const funcs = await window.apiFetch('/funcionarios');
    criarTabela('tabelaFuncionarios', 5, funcs, f => `
      <td style="font-weight:600;">#${f.id}</td>
      <td>${f.nome}</td>
      <td>${f.cpf}</td>
      <td>${f.cargo}</td>
      <td>
        <button class="btn btn-secondary" style="padding:4px 10px; font-size:0.8rem;"
          onclick="excluirFuncionario(${f.id})">🗑</button>
      </td>
    `);
  } catch(err) { window.showToast(err.message || 'Erro ao comunicar com o servidor.', 'error'); }
}

document.addEventListener('DOMContentLoaded', () => {
  const formFuncionario = document.getElementById('formFuncionario');
  if (formFuncionario) {
    formFuncionario.addEventListener('submit', async (e) => {
      e.preventDefault();
      const nome  = document.getElementById('funcNome').value;
      const cpf   = document.getElementById('funcCpf').value;
      const cargo = document.getElementById('funcCargo').value;
      try {
        await window.apiFetch('/funcionarios', {
          method: 'POST',
          body: JSON.stringify({ nome, cpf, cargo, ativo: true })
        });
        window.showToast('Funcionário cadastrado!', 'success');
        fecharModal('modalFuncionario');
        loadFuncionarios();
      } catch(err) { window.showToast(err.message || 'Erro ao comunicar com o servidor.', 'error'); }
    });
  }
});

window.excluirFuncionario = async function(id) {
  if (!confirm('Excluir este funcionário?')) return;
  try {
    await window.apiFetch(`/funcionarios/${id}`, { method: 'DELETE' });
    window.showToast('Funcionário excluído!', 'success');
    loadFuncionarios();
  } catch(err) { window.showToast(err.message || 'Erro ao comunicar com o servidor.', 'error'); }
};

// ─── FORNECEDORES ─────────────────────────────────────────────────────────────
async function loadFornecedores() {
  try {
    const forn = await window.apiFetch('/fornecedores');
    criarTabela('tabelaFornecedores', 4, forn, f => `
      <td style="font-weight:600;">#${f.id}</td>
      <td>${f.nome}</td>
      <td>${f.cnpj}</td>
      <td>
        <button class="btn btn-secondary" style="padding:4px 10px; font-size:0.8rem;"
          onclick="excluirFornecedor(${f.id})">🗑</button>
      </td>
    `);
  } catch(err) { window.showToast(err.message || 'Erro ao comunicar com o servidor.', 'error'); }
}

document.addEventListener('DOMContentLoaded', () => {
  const formFornecedor = document.getElementById('formFornecedor');
  if (formFornecedor) {
    formFornecedor.addEventListener('submit', async (e) => {
      e.preventDefault();
      const nome = document.getElementById('fornNome').value;
      const cnpj = document.getElementById('fornCnpj').value;
      try {
        await window.apiFetch('/fornecedores', {
          method: 'POST',
          body: JSON.stringify({ nome, cnpj, ativo: true })
        });
        window.showToast('Fornecedor cadastrado!', 'success');
        fecharModal('modalFornecedor');
        loadFornecedores();
      } catch(err) { window.showToast(err.message || 'Erro ao comunicar com o servidor.', 'error'); }
    });
  }
});

window.excluirFornecedor = async function(id) {
  if (!confirm('Excluir este fornecedor?')) return;
  try {
    await window.apiFetch(`/fornecedores/${id}`, { method: 'DELETE' });
    window.showToast('Fornecedor excluído!', 'success');
    loadFornecedores();
  } catch(err) { window.showToast(err.message || 'Erro ao comunicar com o servidor.', 'error'); }
};

// ─── FORMAS DE PAGAMENTO ──────────────────────────────────────────────────────
async function loadFormasPagamento() {
  try {
    const fps = await window.apiFetch('/formas-pagamento');
    criarTabela('tabelaPagamentos', 3, fps, fp => `
      <td style="font-weight:600;">#${fp.id}</td>
      <td>${fp.tipo}</td>
      <td>
        <button class="btn btn-secondary" style="padding:4px 10px; font-size:0.8rem;"
          onclick="excluirFormaPagamento(${fp.id})">🗑</button>
      </td>
    `);
  } catch(err) { window.showToast(err.message || 'Erro ao comunicar com o servidor.', 'error'); }
}

document.addEventListener('DOMContentLoaded', () => {
  const formPagamento = document.getElementById('formPagamento');
  if (formPagamento) {
    formPagamento.addEventListener('submit', async (e) => {
      e.preventDefault();
      const tipo = document.getElementById('pagTipo').value;
      try {
        await window.apiFetch('/formas-pagamento', {
          method: 'POST',
          body: JSON.stringify({ tipo })
        });
        window.showToast('Forma de pagamento cadastrada!', 'success');
        fecharModal('modalPagamento');
        loadFormasPagamento();
      } catch(err) { window.showToast(err.message || 'Erro ao comunicar com o servidor.', 'error'); }
    });
  }
});

window.excluirFormaPagamento = async function(id) {
  if (!confirm('Excluir esta forma de pagamento?')) return;
  try {
    await window.apiFetch(`/formas-pagamento/${id}`, { method: 'DELETE' });
    window.showToast('Forma de pagamento excluída!', 'success');
    loadFormasPagamento();
  } catch(err) { window.showToast(err.message || 'Erro ao comunicar com o servidor.', 'error'); }
};

// ─── ESTOQUE ──────────────────────────────────────────────────────────────────
async function loadEstoque() {
  try {
    const estoque = await window.apiFetch('/estoque');
    criarTabela('tabelaEstoque', 4, estoque, e => `
      <td>${e.produto ? e.produto.nome : '#' + e.id}</td>
      <td style="font-weight:bold; color: ${e.quantidade <= 5 ? '#e53e3e' : 'inherit'};">
        ${e.quantidade}
      </td>
      <td>R$ ${Number(e.valorUnitario || 0).toFixed(2).replace('.',',')}</td>
      <td>R$ ${Number(e.total || 0).toFixed(2).replace('.',',')}</td>
    `);
  } catch(err) { window.showToast(err.message || 'Erro ao comunicar com o servidor.', 'error'); }
}

window.abrirModalEntradaEstoque = async function() {
  try {
    const produtos = await window.apiFetch('/produtos');
    const sel = document.getElementById('estoqueProduto');
    sel.innerHTML = '<option value="">Selecione...</option>';
    produtos.forEach(p => { sel.innerHTML += `<option value="${p.id}">${p.nome}</option>`; });
  } catch(err) { window.showToast(err.message || 'Erro ao comunicar com o servidor.', 'error'); }
  abrirModal('modalEntradaEstoque');
};

document.addEventListener('DOMContentLoaded', () => {
  const formEntrada = document.getElementById('formEntradaEstoque');
  if (formEntrada) {
    formEntrada.addEventListener('submit', async (e) => {
      e.preventDefault();
      const idProduto    = Number(document.getElementById('estoqueProduto').value);
      const quantidade   = Number(document.getElementById('estoqueQtd').value);
      const origem       = document.getElementById('estoqueOrigem').value;
      const valorUnitario = parseFloat(document.getElementById('estoqueValor').value);
      try {
        await window.apiFetch('/estoque/entrada', {
          method: 'POST',
          body: JSON.stringify({ idProduto, quantidade, origem, valorUnitario })
        });
        window.showToast('Entrada de estoque registrada!', 'success');
        fecharModal('modalEntradaEstoque');
        loadEstoque();
      } catch(err) { window.showToast(err.message || 'Erro ao comunicar com o servidor.', 'error'); }
    });
  }
});

// ─── USUÁRIOS ─────────────────────────────────────────────────────────────────
async function loadUsuarios() {
  try {
    const usuarios = await window.apiFetch('/usuarios');
    criarTabela('tabelaUsuarios', 4, usuarios, u => `
      <td style="font-weight:600;">#${u.id}</td>
      <td>${u.login}</td>
      <td>${u.perfil}</td>
      <td>${u.ativo ? '✅ Ativo' : '❌ Inativo'}</td>
    `);
  } catch(err) { window.showToast(err.message || 'Erro ao comunicar com o servidor.', 'error'); }
}

document.addEventListener('DOMContentLoaded', () => {
  const formUsuario = document.getElementById('formUsuario');
  if (formUsuario) {
    formUsuario.addEventListener('submit', async (e) => {
      e.preventDefault();
      const login  = document.getElementById('usuLogin').value;
      const senha  = document.getElementById('usuSenha').value;
      const perfil = document.getElementById('usuPerfil').value;
      try {
        await window.apiFetch('/auth/cadastro', {
          method: 'POST',
          body: JSON.stringify({ login, senha, perfil })
        });
        window.showToast('Usuário cadastrado!', 'success');
        fecharModal('modalUsuario');
        loadUsuarios();
      } catch(err) { window.showToast(err.message || 'Erro ao comunicar com o servidor.', 'error'); }
    });
  }
});

// Logout
window.fazerLogout = function() {
  localStorage.removeItem('admin_autenticado');
  localStorage.removeItem('admin_perfil');
  localStorage.removeItem('admin_nome');
  window.location.href = 'index.html';
};
