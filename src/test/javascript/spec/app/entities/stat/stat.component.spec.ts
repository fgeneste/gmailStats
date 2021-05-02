/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';

import * as config from '@/shared/config/config';
import StatComponent from '@/entities/stat/stat.vue';
import StatClass from '@/entities/stat/stat.component';
import StatService from '@/entities/stat/stat.service';

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
  describe('Stat Management Component', () => {
    let wrapper: Wrapper<StatClass>;
    let comp: StatClass;
    let statServiceStub: SinonStubbedInstance<StatService>;

    beforeEach(() => {
      statServiceStub = sinon.createStubInstance<StatService>(StatService);
      statServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<StatClass>(StatComponent, {
        store,
        i18n,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          statService: () => statServiceStub,
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      statServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllStats();
      await comp.$nextTick();

      // THEN
      expect(statServiceStub.retrieve.called).toBeTruthy();
      expect(comp.stats[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      statServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      comp.removeStat();
      await comp.$nextTick();

      // THEN
      expect(statServiceStub.delete.called).toBeTruthy();
      expect(statServiceStub.retrieve.callCount).toEqual(1);
    });
  });
});
