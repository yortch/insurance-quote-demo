const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080';

class QuoteApiError extends Error {
  constructor(message, status, data) {
    super(message);
    this.name = 'QuoteApiError';
    this.status = status;
    this.data = data;
  }
}

async function handleResponse(response) {
  if (!response.ok) {
    let errorData;
    try {
      errorData = await response.json();
    } catch {
      errorData = { message: 'An error occurred' };
    }
    throw new QuoteApiError(
      errorData.message || `HTTP ${response.status}: ${response.statusText}`,
      response.status,
      errorData,
    );
  }
  return response.json();
}

export async function createQuote(quoteData) {
  try {
    const response = await fetch(`${API_BASE_URL}/api/quotes`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(quoteData),
    });
    return await handleResponse(response);
  } catch (error) {
    if (error instanceof QuoteApiError) {
      throw error;
    }
    throw new QuoteApiError('Unable to connect to server. Please try again later.', 0, {});
  }
}

export async function getQuote(id) {
  try {
    const response = await fetch(`${API_BASE_URL}/api/quotes/${id}`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
      },
    });
    return await handleResponse(response);
  } catch (error) {
    if (error instanceof QuoteApiError) {
      throw error;
    }
    throw new QuoteApiError('Unable to connect to server. Please try again later.', 0, {});
  }
}

export async function getAllQuotes(params = {}) {
  try {
    const queryString = new URLSearchParams(params).toString();
    const url = `${API_BASE_URL}/api/quotes${queryString ? `?${queryString}` : ''}`;
    const response = await fetch(url, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
      },
    });
    return await handleResponse(response);
  } catch (error) {
    if (error instanceof QuoteApiError) {
      throw error;
    }
    throw new QuoteApiError('Unable to connect to server. Please try again later.', 0, {});
  }
}
