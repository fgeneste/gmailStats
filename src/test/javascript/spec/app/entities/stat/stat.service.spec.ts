/* tslint:disable max-line-length */
import axios from 'axios';
import sinon from 'sinon';
import dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from '@/shared/date/filters';
import StatService from '@/entities/stat/stat.service';
import { Stat } from '@/shared/model/stat.model';

const error = {
  response: {
    status: null,
    data: {
      type: null,
    },
  },
};

const axiosStub = {
  get: sinon.stub(axios, 'get'),
  post: sinon.stub(axios, 'post'),
  put: sinon.stub(axios, 'put'),
  patch: sinon.stub(axios, 'patch'),
  delete: sinon.stub(axios, 'delete'),
};

describe('Service Tests', () => {
  describe('Stat Service', () => {
    let service: StatService;
    let elemDefault;
    let currentDate: Date;

    beforeEach(() => {
      service = new StatService();
      currentDate = new Date();
      elemDefault = new Stat(0, 'AAAAAAA', 0, currentDate);
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = Object.assign(
          {
            lastupdated: dayjs(currentDate).format(DATE_TIME_FORMAT),
          },
          elemDefault
        );
        axiosStub.get.resolves({ data: returnedFromService });

        return service.find(123).then(res => {
          expect(res).toMatchObject(elemDefault);
        });
      });

      it('should not find an element', async () => {
        axiosStub.get.rejects(error);
        return service
          .find(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should create a Stat', async () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            lastupdated: dayjs(currentDate).format(DATE_TIME_FORMAT),
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            lastupdated: currentDate,
          },
          returnedFromService
        );

        axiosStub.post.resolves({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a Stat', async () => {
        axiosStub.post.rejects(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a Stat', async () => {
        const returnedFromService = Object.assign(
          {
            from: 'BBBBBB',
            number: 1,
            lastupdated: dayjs(currentDate).format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            lastupdated: currentDate,
          },
          returnedFromService
        );
        axiosStub.put.resolves({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a Stat', async () => {
        axiosStub.put.rejects(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a Stat', async () => {
        const patchObject = Object.assign({}, new Stat());
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            lastupdated: currentDate,
          },
          returnedFromService
        );
        axiosStub.patch.resolves({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a Stat', async () => {
        axiosStub.patch.rejects(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of Stat', async () => {
        const returnedFromService = Object.assign(
          {
            from: 'BBBBBB',
            number: 1,
            lastupdated: dayjs(currentDate).format(DATE_TIME_FORMAT),
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            lastupdated: currentDate,
          },
          returnedFromService
        );
        axiosStub.get.resolves([returnedFromService]);
        return service.retrieve().then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of Stat', async () => {
        axiosStub.get.rejects(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a Stat', async () => {
        axiosStub.delete.resolves({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a Stat', async () => {
        axiosStub.delete.rejects(error);

        return service
          .delete(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });
    });
  });
});
