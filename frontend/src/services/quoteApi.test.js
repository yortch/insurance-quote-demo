import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest';
import { createQuote, getQuote, getAllQuotes } from './quoteApi';

describe('quoteApi', () => {
  beforeEach(() => {
    global.fetch = vi.fn();
  });

  afterEach(() => {
    vi.restoreAllMocks();
  });

  describe('createQuote', () => {
    it('sends POST request with quote data', async () => {
      const mockQuote = {
        firstName: 'John',
        lastName: 'Doe',
        email: 'john@example.com',
        insuranceType: 'AUTO',
        coverageLevel: 'COMPREHENSIVE',
      };

      const mockResponse = {
        id: 1,
        ...mockQuote,
        monthlyPremium: 150.0,
      };

      global.fetch.mockResolvedValueOnce({
        ok: true,
        json: async () => mockResponse,
      });

      const result = await createQuote(mockQuote);

      expect(global.fetch).toHaveBeenCalledWith(
        expect.stringContaining('/api/quotes'),
        expect.objectContaining({
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify(mockQuote),
        })
      );

      expect(result).toEqual(mockResponse);
    });

    it('throws QuoteApiError on failed response', async () => {
      const errorData = { message: 'Validation failed' };

      global.fetch.mockResolvedValueOnce({
        ok: false,
        status: 400,
        statusText: 'Bad Request',
        json: async () => errorData,
      });

      await expect(createQuote({})).rejects.toThrow('Validation failed');
    });

    it('throws QuoteApiError on network error', async () => {
      global.fetch.mockRejectedValueOnce(new Error('Network error'));

      await expect(createQuote({})).rejects.toThrow('Unable to connect to server');
    });

    it('handles error response without json body', async () => {
      global.fetch.mockResolvedValueOnce({
        ok: false,
        status: 500,
        statusText: 'Internal Server Error',
        json: async () => {
          throw new Error('Invalid JSON');
        },
      });

      await expect(createQuote({})).rejects.toThrow('An error occurred');
    });
  });

  describe('getQuote', () => {
    it('sends GET request for specific quote', async () => {
      const mockQuote = {
        id: 1,
        firstName: 'John',
        lastName: 'Doe',
        email: 'john@example.com',
        monthlyPremium: 150.0,
      };

      global.fetch.mockResolvedValueOnce({
        ok: true,
        json: async () => mockQuote,
      });

      const result = await getQuote(1);

      expect(global.fetch).toHaveBeenCalledWith(
        expect.stringContaining('/api/quotes/1'),
        expect.objectContaining({
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
          },
        })
      );

      expect(result).toEqual(mockQuote);
    });

    it('throws QuoteApiError when quote not found', async () => {
      global.fetch.mockResolvedValueOnce({
        ok: false,
        status: 404,
        statusText: 'Not Found',
        json: async () => ({ message: 'Quote not found' }),
      });

      await expect(getQuote(999)).rejects.toThrow('Quote not found');
    });

    it('throws QuoteApiError on network error', async () => {
      global.fetch.mockRejectedValueOnce(new Error('Network error'));

      await expect(getQuote(1)).rejects.toThrow('Unable to connect to server');
    });
  });

  describe('getAllQuotes', () => {
    it('sends GET request for all quotes', async () => {
      const mockQuotes = [
        { id: 1, firstName: 'John', lastName: 'Doe' },
        { id: 2, firstName: 'Jane', lastName: 'Smith' },
      ];

      global.fetch.mockResolvedValueOnce({
        ok: true,
        json: async () => mockQuotes,
      });

      const result = await getAllQuotes();

      expect(global.fetch).toHaveBeenCalledWith(
        expect.stringContaining('/api/quotes'),
        expect.objectContaining({
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
          },
        })
      );

      expect(result).toEqual(mockQuotes);
    });

    it('sends GET request with query parameters', async () => {
      const mockQuotes = [{ id: 1, firstName: 'John', lastName: 'Doe' }];

      global.fetch.mockResolvedValueOnce({
        ok: true,
        json: async () => mockQuotes,
      });

      const params = { insuranceType: 'AUTO', status: 'PENDING' };
      const result = await getAllQuotes(params);

      expect(global.fetch).toHaveBeenCalledWith(
        expect.stringContaining('insuranceType=AUTO'),
        expect.anything()
      );

      expect(global.fetch).toHaveBeenCalledWith(
        expect.stringContaining('status=PENDING'),
        expect.anything()
      );

      expect(result).toEqual(mockQuotes);
    });

    it('handles empty query parameters', async () => {
      const mockQuotes = [];

      global.fetch.mockResolvedValueOnce({
        ok: true,
        json: async () => mockQuotes,
      });

      const result = await getAllQuotes({});

      expect(global.fetch).toHaveBeenCalledWith(
        expect.not.stringContaining('?'),
        expect.anything()
      );

      expect(result).toEqual(mockQuotes);
    });

    it('throws QuoteApiError on failed response', async () => {
      global.fetch.mockResolvedValueOnce({
        ok: false,
        status: 500,
        statusText: 'Internal Server Error',
        json: async () => ({ message: 'Server error' }),
      });

      await expect(getAllQuotes()).rejects.toThrow('Server error');
    });

    it('throws QuoteApiError on network error', async () => {
      global.fetch.mockRejectedValueOnce(new Error('Network error'));

      await expect(getAllQuotes()).rejects.toThrow('Unable to connect to server');
    });
  });

  describe('error handling', () => {
    it('preserves error status code', async () => {
      global.fetch.mockResolvedValueOnce({
        ok: false,
        status: 403,
        statusText: 'Forbidden',
        json: async () => ({ message: 'Access denied' }),
      });

      try {
        await createQuote({});
      } catch (error) {
        expect(error.status).toBe(403);
        expect(error.name).toBe('QuoteApiError');
      }
    });

    it('includes error data in QuoteApiError', async () => {
      const errorData = {
        message: 'Validation failed',
        errors: ['Invalid email', 'Invalid phone'],
      };

      global.fetch.mockResolvedValueOnce({
        ok: false,
        status: 400,
        statusText: 'Bad Request',
        json: async () => errorData,
      });

      try {
        await createQuote({});
      } catch (error) {
        expect(error.data).toEqual(errorData);
        expect(error.data.errors).toHaveLength(2);
      }
    });
  });
});
