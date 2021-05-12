/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';

import * as config from '@/shared/config/config';
import ConfComponent from '@/entities/conf/conf.vue';
import ConfClass from '@/entities/conf/conf.component';
import ConfService from '@/entities/conf/conf.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('b-badge', {});
localVue.directive('b-modal', {});
localVue.component('b-button', {});
localVue.component('router-link', {});

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  describe('Conf Management Component', () => {
    let wrapper: Wrapper<ConfClass>;
    let comp: ConfClass;
    let confServiceStub: SinonStubbedInstance<ConfService>;

    beforeEach(() => {
      confServiceStub = sinon.createStubInstance<ConfService>(ConfService);
      confServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<ConfClass>(ConfComponent, {
        store,
        i18n,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          confService: () => confServiceStub,
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      confServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllConfs();
      await comp.$nextTick();

      // THEN
      expect(confServiceStub.retrieve.called).toBeTruthy();
      expect(comp.confs[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      confServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      comp.removeConf();
      await comp.$nextTick();

      // THEN
      expect(confServiceStub.delete.called).toBeTruthy();
      expect(confServiceStub.retrieve.callCount).toEqual(1);
    });
  });
});
