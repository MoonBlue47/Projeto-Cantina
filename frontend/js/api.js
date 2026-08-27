const BASE_URL = 'http://localhost:8080/api';

// Funções de Utilitário Global
function showToast(message, type = 'info') {
  const container = document.getElementById('toast-container');
  if (!container) return;

  const toast = document.createElement('div');
  toast.className = `toast ${type}`;
  
  let icon = 'ℹ️';
  if (type === 'success') icon = '✅';
  if (type === 'error') icon = '❌';

  toast.innerHTML = `
    <span style="font-size: 1.5rem;">${icon}</span>
    <div style="font-weight: 600;">${message}</div>
  `;

  container.appendChild(toast);

  setTimeout(() => {
    toast.style.animation = 'slideOut 0.3s cubic-bezier(0.175, 0.885, 0.32, 1.275) forwards';
    setTimeout(() => toast.remove(), 300);
  }, 3000);
}

// Wrapper para requisições Fetch
async function apiFetch(endpoint, options = {}) {
  try {
    const url = endpoint.startsWith('http') ? endpoint : `${BASE_URL}${endpoint}`;
    
    const headers = {
      'Content-Type': 'application/json',
      ...options.headers
    };

    const response = await fetch(url, { ...options, headers });
    
    // Tratamento genérico de erros
    if (!response.ok) {
      throw new Error(`Erro na requisição: ${response.statusText}`);
    }

    // Se for 204 No Content, retorna nulo
    if (response.status === 204) return null;

    return await response.json();
  } catch (error) {
    console.error('API Error:', error);
    showToast(error.message || 'Ocorreu um erro inesperado.', 'error');
    throw error;
  }
}

// Mocks para simulação visual sem backend
const mockProducts = [
  { id: 1, nome: 'Coxinha de Frango', descricao: 'Salgado frito na hora', preco: 6.50, categoria: 'Salgados' },
  { id: 2, nome: 'Suco de Laranja', descricao: 'Copo 400ml natural', preco: 5.00, categoria: 'Bebidas' },
  { id: 3, nome: 'Salada de Frutas', descricao: 'Frutas frescas da estação', preco: 8.00, categoria: 'Saudáveis' },
  { id: 4, nome: 'Bolo de Chocolate', descricao: 'Fatia com cobertura', preco: 7.00, categoria: 'Doces' },
  { id: 5, nome: 'Pão de Queijo', descricao: 'Tradicional mineiro', preco: 4.50, categoria: 'Salgados' },
];

window.apiFetch = apiFetch;
window.showToast = showToast;
window.mockProducts = mockProducts;
